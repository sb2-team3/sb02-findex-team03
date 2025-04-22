package com.findex.demo.indexData.index.mapper;

import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;

import com.findex.demo.indexInfo.domain.entity.SourceType;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;


@Component
public class IndexDataUpdateRequestMapper {

  /**
   * IndexDataCreateRequest → IndexData 엔티티로 변환
   * (indexInfo는 외부에서 주입)
   */
  public IndexData toEntity(IndexDataCreateRequest dto, IndexInfo indexInfo) {
    IndexData entity = new IndexData();
    entity.setIndexInfo(indexInfo);

    entity.setBaseDate(dto.getBaseDate());
    entity.setOpenPrice(dto.getMarketPrice());
    entity.setClosePrice(dto.getClosingPrice());
    entity.setHighPrice(dto.getHighPrice());
    entity.setLowPrice(dto.getLowPrice());
    entity.setVersus(dto.getVersus());
    entity.setFluationRate(dto.getFluctuationRate());
    entity.setTradingQuantity(dto.getTradingQuantity());
    entity.setTradingPrice(dto.getTradingPrice());
    entity.setMarketTotalAmount(dto.getMarketTotalAmount());
    entity.setSourceType(SourceType.USER); // 기본 소스 타입 지정

    return entity;
  }
}

