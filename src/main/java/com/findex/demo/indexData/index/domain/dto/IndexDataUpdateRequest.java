package com.findex.demo.indexData.index.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexDataUpdateRequest {

  private Double marketPrice;
  private Double closingPrice;
  private Double highPrice;
  private Double lowPrice;
  private Double versus;
  private Double fluctuationRate;


  private Long tradingQuantity;
  private Long tradingPrice;
  private Long marketTotalAmount;
}

