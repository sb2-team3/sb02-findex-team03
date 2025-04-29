package com.findex.demo.syncJobs.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexData.index.repository.IndexDataRepository;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketIndexDataSyncService {

  private final IndexDataRepository indexDataRepository;
  private final IndexInfoRepository indexInfoRepository;

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  @Value("${external.market-index.service-key}")
  private String serviceKey;

  @Value("${external.market-index.base-url}")
  private String baseUrl;

  @Value("${external.market-index.num-of-rows:100}")
  private int numOfRows;

  public void fetchIndexData(String baseDate, List<String> indexNames) {
    log.info("[시작] 마켓 인덱스 데이터 수집 - 기준일: {}, 대상 인덱스: {}", baseDate, indexNames);

    Set<String> seenKeys = new HashSet<>();
    int totalPages = (int) Math.ceil((double) 100 / numOfRows); // TODO: 총 개수 동적으로 바꾸기

    for (int page = 1; page <= totalPages; page++) {
      try {
        String apiUrl = String.format("%s?serviceKey=%s&resultType=json&pageNo=%d&numOfRows=%d&basDt=%s",
            baseUrl, serviceKey, page, numOfRows, baseDate);
        URI uri = new URI(apiUrl);

        log.debug("[API 호출] 페이지 {}: {}", page, apiUrl);

        String response = restTemplate.getForObject(uri, String.class);

        JsonNode items = objectMapper.readTree(response)
            .path("response").path("body").path("items").path("item");

        if (items.isMissingNode() || items.isNull()) {
          log.warn("[경고] 페이지 {}에 데이터 없음", page);
          continue;
        }

        if (items.isArray()) {
          for (JsonNode item : items) {
            processItem(item, seenKeys, indexNames, baseDate);
          }
        } else {
          processItem(items, seenKeys, indexNames, baseDate);
        }

        log.info("[성공] 페이지 {} 처리 완료", page);

      } catch (Exception e) {
        log.error("[실패] 페이지 {} 처리 중 예외 발생: {}", page, e.getMessage(), e);
        throw new CustomException(ErrorCode.PATH_NOT_FOUND, "API 호출 중 오류 발생");
      }
    }

    log.info("[완료] 마켓 인덱스 데이터 수집 종료 - 기준일: {}", baseDate);
  }

  private void processItem(JsonNode item, Set<String> seenKeys, List<String> indexNames, String baseDate) {
    String indexClassification = item.path("idxCsf").asText();
    String indexName = item.path("idxNm").asText();
    String itemDate = item.path("basDt").asText();
    String key = indexClassification + "|" + indexName;

    if (!itemDate.equals(baseDate)) {
      log.debug("[스킵] 기준일 불일치: {}", key);
      return;
    }

    if (!indexNames.isEmpty() && !indexNames.contains(indexName)) {
      log.debug("[스킵] 대상 인덱스 아님: {}", indexName);
      return;
    }

    if (!seenKeys.add(key)) {
      log.debug("[중복 스킵] {}", key);
      return;
    }

    if (!indexInfoRepository.existsByIndexClassificationAndIndexName(indexClassification, indexName)) {
      log.warn("[경고] 존재하지 않는 IndexInfo: {}", key);
      return;
    }

    Optional<IndexInfo> optionalInfo =
        indexInfoRepository.findByIndexClassificationAndIndexName(indexClassification, indexName);
    if (optionalInfo.isEmpty()) {
      log.warn("[경고] IndexInfo 조회 실패: {}", key);
      return;
    }

    IndexInfo indexInfo = optionalInfo.get();

    DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate parsedBaseDate = LocalDate.parse(itemDate, yyyyMMdd);

    if (indexDataRepository.existsByIndexInfoAndBaseDate(indexInfo, parsedBaseDate)) {
      log.debug("[스킵] 이미 존재하는 IndexData: {} ({})", key, baseDate);
      return;
    }

    try {
      ExternalIndexDataDto dto = ExternalIndexDataDto.builder()
          .indexInfo(indexInfo)
          .closePrice(item.path("clpr").asDouble())
          .lowPrice(item.path("lopr").asDouble())
          .openPrice(item.path("mkp").asDouble())
          .highPrice(item.path("hipr").asDouble())
          .fluctuationRate(item.path("fltRt").asDouble())
          .versus(item.path("vs").asDouble())
          .sourceType(SourceType.OPEN_API)
          .tradingPrice(item.path("trPrc").asLong())
          .baseDate(parsedBaseDate)
          .marketTotalAmount(item.path("lstgMrktTotAmt").asLong())
          .tradingQuantity(item.path("trqu").asLong())
          .build();

      IndexData indexData = OpenApiIndexDataMapper.toIndexData(dto);

      indexDataRepository.save(indexData);
      log.info("[저장 성공] IndexData 저장 완료: {} ({})", key, baseDate);

    } catch (DataIntegrityViolationException e) {
      log.warn("[무시] 무결성 제약 위반: {} ({}) - {}", key, baseDate, e.getMessage());
    } catch (Exception e) {
      log.error("[실패] IndexData 저장 실패: {} ({}) - {}", key, baseDate, e.getMessage(), e);
    }
  }
}
