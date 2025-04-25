package com.findex.demo.indexData.index.domain.entity;

import com.findex.demo.global.times.BaseTimeEntity;
import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.dto.IndexDataUpdateRequest;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import jakarta.persistence.Column;
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
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class IndexData extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "index_info_id")
  private IndexInfo indexInfo;

  @Column(nullable = false, name ="base_date")
  private LocalDate baseDate;

  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  private Double openPrice;
  private Double closePrice;
  private Double highPrice;
  private Double lowPrice;

  private Double versus;
  private Double fluctuationRate;

  private Long tradingQuantity;
  @Column(nullable = false, name = "trading_price")
  private Long tradingPrice;
  private Long marketTotalAmount;

  @Builder
  public IndexData(IndexInfo indexInfo, LocalDate baseDate, SourceType sourceType, Double openPrice,
                   Double closePrice, Double highPrice, Double lowPrice, Double versus, Double fluctuationRate,
                   Long tradingQuantity, Long tradingPrice, Long marketTotalAmount) {
    this.indexInfo = indexInfo;
    this.baseDate = baseDate;
    this.sourceType = sourceType;
    this.openPrice = openPrice;
    this.closePrice = closePrice;
    this.highPrice = highPrice;
    this.lowPrice = lowPrice;
    this.versus = versus;
    this.fluctuationRate = fluctuationRate;
    this.tradingQuantity = tradingQuantity;
    this.tradingPrice = tradingPrice;
    this.marketTotalAmount = marketTotalAmount;
  }


  public void update(IndexDataUpdateRequest request) {
    if (request.getMarketPrice() != null) {
      this.openPrice = request.getMarketPrice();
    }

    if (request.getClosingPrice() != null) {
      this.closePrice = request.getClosingPrice();
    }

    if (request.getHighPrice() != null) {
      this.highPrice = request.getHighPrice();
    }

    if (request.getLowPrice() != null) {
      this.lowPrice = request.getLowPrice();
    }

    if (request.getVersus() != null) {
      this.versus = request.getVersus();
    }

    if (request.getFluctuationRate() != null) {
      this.fluctuationRate = request.getFluctuationRate();
    }

    if (request.getTradingQuantity() != null) {
      this.tradingQuantity = request.getTradingQuantity();
    }

    if (request.getTradingPrice() != null) {
      this.tradingPrice = request.getTradingPrice();
    }

    if (request.getMarketTotalAmount() != null) {
      this.marketTotalAmount = request.getMarketTotalAmount();
    }
  }

}
