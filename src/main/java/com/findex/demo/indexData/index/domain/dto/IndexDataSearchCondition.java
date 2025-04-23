package com.findex.demo.indexData.index.domain.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IndexDataSearchCondition {
  private Integer indexInfoId;
  private LocalDate startDate;
  private LocalDate endDate;
  private Integer idAfter;
  private String cursor;
  private String sortField = "baseDate";
  private String sortDirection = "desc";
  private int size = 10;
}

