package com.findex.demo.indexData.index.domain.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class IndexDataSearchCondition {
  private Long indexInfoId;
  private LocalDate startDate;
  private LocalDate endDate;
  private Long idAfter;
  private String cursor;
  private String sortField = "baseDate";
  private String sortDirection = "desc";
  private int size = 10;
}

