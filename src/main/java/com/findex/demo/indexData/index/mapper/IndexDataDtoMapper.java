package com.findex.demo.indexData.index.mapper;

import com.findex.demo.indexData.index.domain.dto.IndexDataDto;
import com.findex.demo.indexData.index.domain.entity.IndexData;

public class IndexDataDtoMapper implements EntityMapper<IndexDataDto,IndexData> {

  IndexInfoRepositroy indexInfoRepositroy;

  @Override
  public IndexData toEntity(IndexDataDto dto) {
    return null;
  }

  @Override
  public IndexDataDto toDto(IndexData entity) {
    return new IndexDataDto(
      entity.getId(),
      entity.getIndexInfo().getId(),
      entity.getDate(),
      entity.getSourceType(),
      entity.getOpenPrice(),
      entity.getClosePrice(),
      entity.getHighPrice(),
      entity.getLowPrice(),
      entity.getVersus(),
      entity.getFluationRate(),
      entity.getTradingQuantity(),
      entity.getTradingPrice(),
      entity.getMarketTotalAmount()
    );
  }
}
