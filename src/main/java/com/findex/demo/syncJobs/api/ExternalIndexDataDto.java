package com.findex.demo.syncJobs.api;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.Builder;


@Builder
public record ExternalIndexDataDto(
    IndexInfo indexInfo,

    LocalDate baseDate,

    SourceType sourceType,

    Double openPrice,
    Double closePrice,
    Double highPrice,
    Double lowPrice,

    Double versus,
    Double fluationRate,

    Long tradingQuantity,
    Long tradingPrice,
    Long marketTotalAmount
) {


}
