package com.findex.demo.indexData.index.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;


/*
@Service
@RequiredArgsConstructor
public class IndexDataOpenApiService {

  private final RestTemplate restTemplate;
  private final IndexInfoRepository indexInfoRepository;


  private String serviceKey = "36ID8iOnp68nJoKAhT0Ynow39nMtNDM3idhAa9TSjW9MzNS79979CltA7umRWB%2FbyvbLhPjpqLBnbdJeSophrA%3D%3D";

  public IndexDataCreateRequest fetchIndexData(String baseDate, String idxNm) {
    String url = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex"
        + "?serviceKey=" + serviceKey
        + "&resultType=json"
        + "&idxNm=" + UriUtils.encode(idxNm, StandardCharsets.UTF_8)
        + "&basDt=" + baseDate;

    ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, null, JsonNode.class);
    JsonNode item = response.getBody()
        .path("response").path("body").path("items").path("item").get(0);

    // 필요한 값 추출
    Double marketPrice = item.path("mkp").asDouble();
    Double closingPrice = item.path("clpr").asDouble();
    Double highPrice = item.path("hipr").asDouble();
    Double lowPrice = item.path("lopr").asDouble();
    Double versus = item.path("vs").asDouble();
    Double fluctuationRate = item.path("fltRt").asDouble();
    Long tradingQuantity = item.path("trqu").asLong();
    Long tradingPrice = item.path("trPrc").asLong();
    Long marketTotalAmount = item.path("mrktTotAmt").asLong();

    // 예: KOSPI → indexInfoId 조회 (임의 로직)
    IndexInfo info = indexInfoRepository.findByName(idxNm)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "지수명이 올바르지 않습니다."));

    return IndexDataCreateRequest.builder()
        .indexInfoId(info.getId())
        .baseDate(LocalDate.parse(baseDate, DateTimeFormatter.ofPattern("yyyyMMdd")).toString())
        .marketPrice(marketPrice)
        .closingPrice(closingPrice)
        .highPrice(highPrice)
        .lowPrice(lowPrice)
        .versus(versus)
        .fluctuationRate(fluctuationRate)
        .tradingQuantity(tradingQuantity)
        .tradingPrice(tradingPrice)
        .marketTotalAmount(marketTotalAmount)
        .build();
  }
}
*/