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
    Set<String> seenKeys = new HashSet<>();
    int totalPages = (int) Math.ceil((double) 100 / numOfRows); // TODO: 총 개수 동적으로 바꾸기

    for (int page = 1; page <= totalPages; page++) {
      try {
        String apiUrl = String.format("%s?serviceKey=%s&resultType=json&pageNo=%d&numOfRows=%d&basDt=%s",
            baseUrl, serviceKey, page, numOfRows, baseDate);
        URI uri = new URI(apiUrl);
        String response = restTemplate.getForObject(uri, String.class);

        JsonNode items = objectMapper.readTree(response)
            .path("response").path("body").path("items").path("item");

        if (items.isMissingNode() || items.isNull()) {
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
        throw new CustomException(ErrorCode.PATH_NOT_FOUND, "API 호출 중 오류 발생");
      }
    }
  }

  private void processItem(JsonNode item, Set<String> seenKeys, List<String> indexNames, String baseDate) {
    String indexClassification = item.path("idxCsf").asText();
    String indexName = item.path("idxNm").asText();
    String itemDate = item.path("basDt").asText();

    if (!itemDate.equals(baseDate)) {
      return;
    }


    //
    if ( !indexNames.isEmpty() &&!indexNames.contains(indexName)) {
      return;
    }

    String key = indexClassification + "|" + indexName;
    if (!seenKeys.add(key)) {
      return;
    }

    if (!indexInfoRepository.existsByIndexClassificationAndIndexName(indexClassification, indexName)) {
      return;
    }


    List<IndexData> indexDatas = new ArrayList<>();
    Optional<IndexInfo> optionalInfo =
        indexInfoRepository.findByIndexClassificationAndIndexName(indexClassification, indexName);


    IndexInfo indexInfo = optionalInfo.get();

    DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate parsedBaseDate = LocalDate.parse(itemDate, yyyyMMdd);

    if (indexDataRepository.existsByIndexInfoAndBaseDate(indexInfo, parsedBaseDate)) {
      return;
    }

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
      return;
    }
    try {
      for(IndexData indexData : indexDatas) {
        indexDataRepository.save(indexData);
      }
    } catch (DataIntegrityViolationException e) {
    }
  }
}

