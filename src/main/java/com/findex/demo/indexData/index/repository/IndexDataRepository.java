package com.findex.demo.indexData.index.repository;

import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData, Integer>,
    IndexDataRepositoryCustom {

    @Query("SELECT COUNT(i) > 0 FROM IndexData i WHERE i.indexInfo = :indexInfo AND i.baseDate = :baseDate")
    boolean existsByIndexInfoAndBaseDate(@Param("indexInfo") IndexInfo indexInfo,
        @Param("baseDate") LocalDate baseDate);

    List<IndexData> findByIndexInfoAndBaseDateBetweenOrderByBaseDateAsc(IndexInfo indexInfo,
        LocalDate startDate, LocalDate endDate);

    IndexData findByIndexInfoIdAndBaseDateBetween(Integer favoriteIndexId,
        LocalDate startDate, LocalDate endDate);

    List<IndexData> findByIndexInfoInAndBaseDateBetween(List<IndexInfo> indexInfoList,
        LocalDate startDate, LocalDate endDate);

    @Query("""
        SELECT i FROM IndexData i
        WHERE (:indexInfoId IS NULL OR i.indexInfo.id = :indexInfoId)
        AND (:startDate IS NULL OR i.baseDate >= :startDate)
        AND (:endDate IS NULL OR i.baseDate <= :endDate)
        AND (:cursorId IS NULL OR i.id > :cursorId)
        ORDER BY i.id ASC
        """)
    List<IndexData> findWithCursorPaging(
        @Param("indexInfoId") Integer indexInfoId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("cursorId") Integer cursorId,
        Pageable pageable
    );
}
