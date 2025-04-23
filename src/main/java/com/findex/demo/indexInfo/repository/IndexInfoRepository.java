package com.findex.demo.indexInfo.repository;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Integer> {

  @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM IndexInfo e " +
      "WHERE e.indexClassification = :indexClassification AND e.indexName = :indexName")
  boolean existsByIndexClassificationAndIndexName(
      @Param("indexClassification") String indexClassification,
      @Param("indexName") String indexName);

  Optional<IndexInfo> findByIndexClassificationAndIndexName(String indexClassification, String indexName);

  List<IndexInfo> findByFavoriteIsTrue();

  List<IndexInfo> findByIndexClassification(String indexClassification);

  @Query("SELECT i FROM IndexInfo i WHERE i.indexName = :indexName")
  IndexInfo findByIndexName(@Param("indexName") String indexName);

}
