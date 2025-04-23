package com.findex.demo.indexInfo.repository;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Integer> {

  @Query("SELECT COUNT(i) > 0 FROM IndexInfo i WHERE i.indexClassification = :indexClassification AND i.indexName = :indexName")
  boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName);

  List<IndexInfo> findByFavoriteIsTrue();

  List<IndexInfo> findByIndexClassification(String indexClassification);

  List<IndexInfo> findByIndexName(String indexName);
}
