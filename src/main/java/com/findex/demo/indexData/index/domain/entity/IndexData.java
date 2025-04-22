package com.findex.demo.indexData.index.domain.entity;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndexData {

  @Id
  @GeneratedValue
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "index_info_id")
  private IndexInfo indexInfo;

  private String date;

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

}

