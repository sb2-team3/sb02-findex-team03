package com.findex.demo.indexData.index.domain.dto;

import com.findex.demo.indexInfo.domain.entity.SourceType;
import java.time.LocalDate;

public record IndexDataDto(
        Integer id,
        Integer indexInfoId,
        LocalDate baseDate,
        SourceType sourceType,
        Double marketPrice,
        Double closingPrice,
        Double highPrice,
        Double lowPrice,
        Double versus,
        Double fluctuationRate,
        Long tradingQuantity,
        Long tradingPrice,
        Long marketTotalAmount
) {}
