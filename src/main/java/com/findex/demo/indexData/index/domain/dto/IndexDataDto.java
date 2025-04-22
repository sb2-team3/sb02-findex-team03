package com.findex.demo.indexData.index.domain.dto;

import com.findex.demo.indexInfo.domain.entity.SourceType;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
public class IndexDataDto {
  // 고유 식별자 및 참조
  private Integer id;
  private Integer indexInfoId;

  // 날짜 및 소스 타입
  private LocalDate baseDate;
  private SourceType sourceType;

  // 숫자 값 (소수)
  private Double marketPrice;
  private Double closingPrice;
  private Double highPrice;
  private Double lowPrice;
  private Double versus;
  private Double fluctuationRate;

  // 정수 값 (거래 관련)
  private Long tradingQuantity;
  private Long tradingPrice;
  private Long marketTotalAmount;
}
