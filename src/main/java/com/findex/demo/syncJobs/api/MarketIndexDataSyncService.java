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
import java.time.format.DateTimeParseException;
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

  @Value("36ID8iOnp68nJoKAhT0Ynow39nMtNDM3idhAa9TSjW9MzNS79979CltA7umRWB%2FbyvbLhPjpqLBnbdJeSophrA%3D%3D")
  private String serviceKey;

  @Value("${external.market-index.base-url}")
  private String baseUrl;

  @Value("${external.market-index.num-of-rows:100}")
  private int numOfRows;

  public void fetchIndexData(String baseDate, List<String> indexNames) {
    Set<String> seenKeys = new HashSet<>();
    int totalPages = (int) Math.ceil((double) 100 / numOfRows); // TODO: 총 개수 동적으로 바꾸기

    for (int page = 1; page <= totalPages; page++) {
      try {
        String apiUrl = String.format("%s?serviceKey=%s&resultType=json&pageNo=%d&numOfRows=%d&basDt=%s",
            baseUrl, serviceKey, page, numOfRows, baseDate);

        log.info("📤 [Page {}] 요청 URI: {}", page, apiUrl);
        URI uri = new URI(apiUrl);
        String response = restTemplate.getForObject(uri, String.class);

        JsonNode items = objectMapper.readTree(response)
            .path("response").path("body").path("items").path("item");

        if (items.isMissingNode() || items.isNull()) {
          log.warn("⚠️ [Page {}] 'item' 노드 없음", page);
          continue;
        }

        if (items.isArray()) {
          for (JsonNode item : items) {
            processItem(item, seenKeys, indexNames, baseDate);
          }
        } else {
          processItem(items, seenKeys, indexNames, baseDate);
        }

      } catch (Exception e) {
        log.error("❌ [Page {}] API 오류: {}", page, e.getMessage(), e);
        throw new CustomException(ErrorCode.PATH_NOT_FOUND, "API 호출 중 오류 발생");
      }
    }

    log.info("🏁 지수 데이터 연동 완료");
  }

  private void processItem(JsonNode item, Set<String> seenKeys, List<String> indexNames, String baseDate) {
    String indexClassification = item.path("idxCsf").asText();
    String indexName = item.path("idxNm").asText();
    String itemDate = item.path("basDt").asText();

    if (!itemDate.equals(baseDate)) {
      log.debug("📅 날짜 불일치로 건너뜀: {}, 기대값: {}", itemDate, baseDate);
      return;
    }

    //
    if ( !indexNames.isEmpty() &&!indexNames.contains(indexName)) {
      log.debug("🔍 필터링된 지수로 제외됨: {}", indexName);
      return;
    }

    String key = indexClassification + "|" + indexName;
    if (!seenKeys.add(key)) {
      log.debug("🔁 중복 지수: {}", key);
      return;
    }

    if (!indexInfoRepository.existsByIndexClassificationAndIndexName(indexClassification, indexName)) {
      log.warn("❗ 등록되지 않은 지수: {} - 저장 생략", indexName);
      return;
    }



    List<IndexData> indexDatas = new ArrayList<>();
    Optional<IndexInfo> optionalInfo =
        indexInfoRepository.findByIndexClassificationAndIndexName(indexClassification, indexName);


    IndexInfo indexInfo = optionalInfo.get();

    DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");


    try{
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
          .baseDate(Optional.of(
                  LocalDate.parse(itemDate, yyyyMMdd))
              .orElseThrow(() -> new IllegalArgumentException("baseDate 파싱 실패")))
          .marketTotalAmount(item.path("lstgMrktTotAmt").asLong())
          .tradingQuantity(item.path("trqu").asLong())
          .build();
      indexDatas.add(OpenApiIndexDataMapper.toIndexData(dto));
    }
    catch (Exception e) {
      log.warn("⚠️ ExternalIndexDataDto 오류: {}", key);
      return;
    }


    try {
      for(IndexData indexData : indexDatas) {
        indexDataRepository.save(indexData);
        log.debug("저장 되니");
        log.info("✅ 저장 완료: {}", indexName);
      }
    } catch (DataIntegrityViolationException e) {
      log.warn("⚠️ 중복된 지수 데이터 무시: {}", key);
    }

    // 🔄 이력 저장 로직도 여기에 추가 가능
  }


}

