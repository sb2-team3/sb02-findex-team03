package com.findex.demo.indexData.index.domain.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexDataUpdateRequest {

  @PositiveOrZero(message = "시장 가격(marketPrice)은 음수가 될 수 없습니다.")
  private Double marketPrice;

  @PositiveOrZero(message = "종가(closingPrice)는 음수가 될 수 없습니다.")
  private Double closingPrice;

  @PositiveOrZero(message = "고가(highPrice)는 음수가 될 수 없습니다.")
  private Double highPrice;

  @PositiveOrZero(message = "저가(lowPrice)는 음수가 될 수 없습니다.")
  private Double lowPrice;

  private Double versus;
  private Double fluctuationRate;


  private Long tradingQuantity;
  private Long tradingPrice;
  private Long marketTotalAmount;
}

