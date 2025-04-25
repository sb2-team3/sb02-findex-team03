package com.findex.demo.indexData.index.mapper;

import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.dto.IndexDataDto;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;

public class IndexDataMapper {

  public static IndexDataDto toIndexDto(IndexData entity) {
    Integer indexInfoId = 0;
    try{
      indexInfoId =  entity.getIndexInfo().getId();
    }catch (Exception e){
      e.printStackTrace();
    }
    return new IndexDataDto(
        entity.getId(),
        indexInfoId,
        entity.getBaseDate(),
        entity.getSourceType(),
        entity.getOpenPrice(),
        entity.getClosePrice(),
        entity.getHighPrice(),
        entity.getHighPrice(),
        entity.getVersus(),
        entity.getFluctuationRate(),
        entity.getTradingQuantity(),
        entity.getTradingPrice(),
        entity.getMarketTotalAmount()
    );
  }

  public static IndexData toIndexData(IndexDataCreateRequest dto, IndexInfo indexInfo) {
    return IndexData.builder()
        .indexInfo(indexInfo)
        .baseDate(dto.getBaseDate())
        .openPrice(dto.getMarketPrice())
        .closePrice(dto.getClosingPrice())
        .highPrice(dto.getHighPrice())
        .lowPrice(dto.getLowPrice())
        .versus(dto.getVersus())
        .fluctuationRate(dto.getFluctuationRate())
        .tradingQuantity(dto.getTradingQuantity())
        .tradingPrice(dto.getTradingPrice())
        .marketTotalAmount(dto.getMarketTotalAmount())
        .sourceType(SourceType.USER) // 기본값 지정
        .build();
  }
}

