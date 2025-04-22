package com.findex.demo.indexData.index.mapper;

import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.dto.IndexDataDto;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;

public class IndexDataMapper {


  public IndexDataDto toDto(IndexData entity) {
    return new IndexDataDto(
        entity.getId(),
        entity.getIndexInfo().getId(),
        entity.getBaseDate(),
        entity.getSourceType(),
        entity.getOpenPrice(),
        entity.getClosePrice(),
        entity.getHighPrice(),
        entity.getHighPrice(),
        entity.getVersus(),
        entity.getFluationRate(),
        entity.getTradingQuantity(),
        entity.getTradingPrice(),
        entity.getMarketTotalAmount()
    );
  }


}
