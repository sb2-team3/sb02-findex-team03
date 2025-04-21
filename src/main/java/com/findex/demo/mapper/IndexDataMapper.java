package com.findex.demo.mapper;

import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;

@NoArgsConstructor
public class IndexDataMapper implements EntityMapper<IndexDataCreateRequest,IndexData> {

  IndexInfoRepositroy indexInfoRepositroy;

  @Override
  public IndexData toEntity(IndexDataCreateRequest dto) {
    IndexData data = new IndexData();
    data.setIndexInfo(dto.getIndexInfoId());
    data.setDate(dto.getBaseDate());
    data.setOpenPrice(dto.getMarketPrice());
    data.setClosePrice(dto.getMarketPrice());
    data.setHighPrice(dto.getHighPrice());
    data.setLowPrice(dto.getLowPrice());
    data.setVersus(dto.getVersus());
    data.setFluationRate(dto.getFluctuationRate());
    data.setTradingQuantity(dto.getTradingQuantity());
    data.setTradingPrice(dto.getTradingPrice());
    data.setMarketTotalAmount(dto.getMarketTotalAmount());
    data.setSourceType(SourceType.USER);
    return data;
  }

  @Override
  public IndexDataCreateRequest toDto(IndexData entity) {
    return null;
  }
}
