package com.findex.demo.indexData.index.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Schema(description = "지수 데이터 생성 요청 DTO")
public record IndexDataCreateRequest(
    @Schema(description = "지수 정보 ID", example = "1")
    Integer indexInfoId,

    @Schema(description = "기준 일자", example = "2024-04-21")
    String baseDate,

    @Schema(description = "시가", example = "2500.75")
    Double marketPrice,

    @Schema(description = "종가", example = "2550.00")
    Double closingPrice,

    @Schema(description = "고가", example = "2575.00")
    Double highPrice,

    @Schema(description = "저가", example = "2480.50")
    Double lowPrice,

    @Schema(description = "전일 대비 등락", example = "-5.25")
    Double versus,

    @Schema(description = "등락률", example = "-0.25")
    Double fluctuationRate,

    @Schema(description = "총 거래량", example = "120000000")
    Long tradingQuantity,

    @Schema(description = "총 거래대금", example = "150000000000")
    Long tradingPrice,

    @Schema(description = "상장 시가 총액", example = "2100000000000")
    Long marketTotalAmount
) {


}
