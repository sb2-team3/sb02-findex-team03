package com.findex.demo.indexData.index.domain.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

public class IndexData {

  @Id
  @GeneratedValue
  private Long id;

  //@ManyToOne
  //@JoinColumn(name = "index_info_id")
  //private IndexInfo indexInfo;

  private Instant date;

  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  private BigDecimal openPrice;
  private BigDecimal closePrice;
  private BigDecimal highPrice;
  private BigDecimal lowPrice;

  private BigDecimal changeAmount;
  private BigDecimal changeRate;

  private Long volume;
  private Long tradingValue;
  private Long marketCap;

}
