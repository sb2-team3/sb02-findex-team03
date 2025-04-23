package com.findex.demo.indexData.index.repository;

import com.findex.demo.indexData.index.domain.dto.IndexDataSearchCondition;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import java.awt.print.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;

public interface IndexDataRepositoryCustom {
  List<IndexData> findByCondition(IndexDataSearchCondition condition, Pageable pageable);

  Integer countByCondition(IndexDataSearchCondition condition);


}
