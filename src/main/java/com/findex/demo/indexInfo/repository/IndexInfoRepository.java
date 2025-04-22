package com.findex.demo.indexInfo.repository;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long> {
  boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName);
}
