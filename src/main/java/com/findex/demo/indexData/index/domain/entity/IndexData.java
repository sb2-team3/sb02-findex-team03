package com.findex.demo.indexData.index.domain.entity;

import com.findex.demo.global.times.BaseTimeEntity;
import com.findex.demo.indexData.index.domain.dto.IndexDataUpdateRequest;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.Builder;


@Entity
@Getter
@Setter
public class IndexData extends BaseTimeEntity {

  @Id
  @GeneratedValue
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "index_info_id")
  private IndexInfo indexInfo;

  private LocalDate baseDate;

  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  private Double openPrice;
  private Double closePrice;
  private Double highPrice;
  private Double lowPrice;

  private Double versus;
  private Double fluationRate;

  private Long tradingQuantity;
  private Long tradingPrice;
  private Long marketTotalAmount;

  public void updateIndexData(IndexDataUpdateRequest request) {
    this.openPrice= request.getMarketPrice();
    this.closePrice = request.getClosingPrice();
    this.highPrice= request.getHighPrice();
    this.lowPrice= request.getLowPrice();
    this.versus=request.getVersus();
    this.fluationRate=request.getFluctuationRate();
    this.tradingQuantity=request.getTradingQuantity();
    this.tradingPrice-=request.getTradingPrice();
    this.marketTotalAmount = request.getMarketTotalAmount();
  }
}

