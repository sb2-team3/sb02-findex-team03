package com.findex.demo.indexData.index.domain.entity;

import com.findex.demo.global.times.BaseTimeEntity;
import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.dto.IndexDataUpdateRequest;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class IndexData extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  @Builder
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

  public void update(IndexDataCreateRequest dto, IndexInfo indexInfo) {
    if (indexInfo != null) {
      this.indexInfo = indexInfo;
    }

    if (dto.getBaseDate() != null) {
      this.baseDate = dto.getBaseDate();
    }

    if (dto.getMarketPrice() != null) {
      this.openPrice = dto.getMarketPrice();
    }

    if (dto.getClosingPrice() != null) {
      this.closePrice = dto.getClosingPrice();
    }

    if (dto.getHighPrice() != null) {
      this.highPrice = dto.getHighPrice();
    }

    if (dto.getLowPrice() != null) {
      this.lowPrice = dto.getLowPrice();
    }

    if (dto.getVersus() != null) {
      this.versus = dto.getVersus();
    }

    if (dto.getFluctuationRate() != null) {
      this.fluationRate = dto.getFluctuationRate();
    }

    if (dto.getTradingQuantity() != null) {
      this.tradingQuantity = dto.getTradingQuantity();
    }

    if (dto.getTradingPrice() != null) {
      this.tradingPrice = dto.getTradingPrice();
    }

    if (dto.getMarketTotalAmount() != null) {
      this.marketTotalAmount = dto.getMarketTotalAmount();
    }

    // 기본값 지정은 null 체크 안 하고 바로 적용
    this.sourceType = SourceType.USER;
  }



}

