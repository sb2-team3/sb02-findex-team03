package com.findex.demo.indexInfo.repository;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Integer> {

  /*
  TODO: 쿼리 문 으로 짜보세요
   */
  boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName);
  Optional<IndexInfo> findByIndexClassificationAndIndexName(String classification, String name);

}
