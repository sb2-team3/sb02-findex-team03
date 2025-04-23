package com.findex.demo.syncJobs.api;

import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OpenApiIndexDataMapper {

  public static IndexData toIndexData(ExternalIndexDataDto externalIndexDataDto) {
    return IndexData.builder()
        .indexInfo(externalIndexDataDto.indexInfo())
        .baseDate(externalIndexDataDto.baseDate())
        .openPrice(externalIndexDataDto.openPrice())
        .closePrice(externalIndexDataDto.closePrice())
        .highPrice(externalIndexDataDto.highPrice())
        .lowPrice(externalIndexDataDto.lowPrice())
        .versus(externalIndexDataDto.versus())
        .fluationRate(externalIndexDataDto.fluationRate())
        .tradingPrice(externalIndexDataDto.tradingPrice())
        .tradingQuantity(externalIndexDataDto.tradingQuantity())
        .marketTotalAmount(externalIndexDataDto.marketTotalAmount())
        .sourceType(SourceType.OPEN_API)
        .build();
  }


}
