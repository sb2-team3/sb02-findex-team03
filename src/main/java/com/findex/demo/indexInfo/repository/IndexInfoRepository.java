package com.findex.demo.indexInfo.repository;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Integer> {

  /*
  TODO: 쿼리 문 으로 짜보세요
   */
  boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName);


  List<IndexInfo> findByFavoriteIsTrue();

  List<IndexInfo> findByIndexClassification(String indexClassification);
}
