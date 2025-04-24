package com.findex.demo.indexInfo.repository;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Integer> {

  @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM IndexInfo e " +
      "WHERE e.indexClassification = :indexClassification AND e.indexName = :indexName")
  boolean existsByIndexClassificationAndIndexName(
      @Param("indexClassification") String indexClassification,
      @Param("indexName") String indexName);

  List<IndexInfo> findByIndexClassificationAndIndexName(String indexClassification, String indexName);

  List<IndexInfo> findByFavoriteIsTrue();

  List<IndexInfo> findByIndexClassification(String indexClassification);

  IndexInfo findByIndexName(String indexName);

  @Query("SELECT i FROM IndexInfo i WHERE " +
      "( :indexClassification IS NULL OR i.indexClassification LIKE %:indexClassification% ) AND " +
      "( :indexName IS NULL OR i.indexName LIKE %:indexName% ) AND " +
      "( :favorite IS NULL OR i.favorite = :favorite OR (i.favorite IS NULL AND :favorite IS FALSE) ) AND " +
      "( :idAfter IS NULL OR i.id > :idAfter ) " + "ORDER BY i.id ASC")
  Page<IndexInfo> findByFilter(
      @Param("indexClassification") String indexClassification,
      @Param("indexName") String indexName,
      @Param("favorite") Boolean favorite,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );

  @Query("SELECT COUNT(i) FROM IndexInfo i WHERE " +
      "( :indexClassification IS NULL OR i.indexClassification LIKE %:indexClassification% ) AND " +
      "( :indexName IS NULL OR i.indexName LIKE %:indexName% ) AND " +
      "( :favorite IS NULL OR i.favorite = :favorite OR (i.favorite IS NULL AND :favorite IS FALSE) ) AND " +
      "( :idAfter IS NULL OR i.id > :idAfter )")
  Long countByFilter(
      @Param("indexClassification") String indexClassification,
      @Param("indexName") String indexName,
      @Param("favorite") Boolean favorite,
      @Param("idAfter") Long idAfter
  );
}
