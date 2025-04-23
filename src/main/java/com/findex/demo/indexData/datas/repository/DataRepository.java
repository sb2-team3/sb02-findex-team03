package com.findex.demo.indexData.datas.repository;

import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends JpaRepository<IndexData, Integer> {

    @Query(
        "SELECT i FROM IndexData i WHERE i.indexInfo = :indexInfo AND i.baseDate "
            + "BETWEEN :startDate AND :endDate ORDER BY i.baseDate ASC")
    List<IndexData> findByIndexInfoAndBaseDateBetweenOrderByBaseDateAsc(
        @Param("indexIfo") IndexInfo indexInfo,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query(
        "SELECT i FROM IndexData i WHERE i.indexInfo.id = :favoriteIndexId AND i.baseDate BETWEEN :startDate "
            + "AND :endDate")
    IndexData findByIndexInfoIdAndBaseDateBetween(
        @Param("favoriteIndexId") Integer favoriteIndexId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query(
        "SELECT i FROM IndexData i WHERE i.indexInfo IN :indexInfoList AND i.baseDate BETWEEN :startDate AND :endDate")
    List<IndexData> findByIndexInfoInAndBaseDateBetween(
        @Param("indexInfoList") List<IndexInfo> indexInfoList,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
}
