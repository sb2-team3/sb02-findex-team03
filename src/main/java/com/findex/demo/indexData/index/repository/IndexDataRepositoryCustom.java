package com.findex.demo.indexData.index.repository;

import com.findex.demo.indexData.index.domain.dto.IndexDataSearchCondition;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;


public interface IndexDataRepositoryCustom {
  List<IndexData> findByCondition(IndexDataSearchCondition condition, Pageable pageable);

  Long countByCondition(IndexDataSearchCondition condition);



  List<IndexData> findWithCursor(IndexInfo indexInfo, LocalDate startDate,
      LocalDate endDate, Integer cursorId, int size);
}
