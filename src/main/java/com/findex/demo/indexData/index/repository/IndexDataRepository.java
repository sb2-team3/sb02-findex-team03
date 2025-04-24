package com.findex.demo.indexData.index.repository;

import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    @Query(
        "SELECT i FROM IndexData i WHERE i.indexInfo = :indexInfo AND i.baseDate "
            + "BETWEEN :startDate AND :endDate ORDER BY i.baseDate ASC")
    List<IndexData> findByIndexInfoAndBaseDateBetweenOrderByBaseDateAsc(IndexInfo indexInfo,
        LocalDate startDate, LocalDate endDate);

    @Query(
        "SELECT i FROM IndexData i WHERE i.indexInfo.id = :favoriteIndexId AND i.baseDate BETWEEN :startDate "
            + "AND :endDate")
    IndexData findByIndexInfoIdAndBaseDateBetween(Integer favoriteIndexId,
        LocalDate startDate, LocalDate endDate);

    @Query(
        "SELECT i FROM IndexData i WHERE i.indexInfo IN :indexInfoList AND i.baseDate BETWEEN :startDate AND :endDate")
    List<IndexData> findByIndexInfoInAndBaseDateBetween(List<IndexInfo> indexInfoList,
        LocalDate startDate, LocalDate endDate);


    @Query("""
        SELECT i FROM IndexData i
        WHERE i.indexInfo.id = :indexInfoId
        AND (:startDate IS NULL OR i.baseDate >= :startDate)
        AND (:endDate IS NULL OR i.baseDate <= :endDate)
        AND (:cursorId IS NULL OR i.id > :cursorId)
        ORDER BY i.id ASC
        """)
    List<IndexData> findWithIndexInfoId(Integer indexInfoId, LocalDate startDate, LocalDate endDate,
        Integer cursorId, Pageable pageable);

    @Query("""
        SELECT i FROM IndexData i
        WHERE i.indexInfo.id = :indexInfoId
        AND (:startDate IS NULL OR i.baseDate >= :startDate)
        AND (:endDate IS NULL OR i.baseDate <= :endDate)
        AND (:cursorId IS NULL OR i.id > :cursorId)
        ORDER BY i.id ASC
        """)
    List<IndexData> findWithoutIndexInfoId(LocalDate startDate, LocalDate endDate, Integer cursorId,
        Pageable pageable);

    List<IndexData> findByIndexInfoIdInAndBaseDateIn(List<Integer> favoriteIndexIds,
        List<LocalDate> startDate);

    List<IndexData> findByIndexInfoInAndBaseDateIn(List<IndexInfo> indexInfoList,
        List<LocalDate> localDates);

    boolean existsByBaseDate(LocalDate date);

    Optional<LocalDate> findMaxBaseDateBeforeDate(LocalDate date);

    Optional<LocalDate> findMinBaseDateAfterDate(LocalDate date);

    Optional<IndexData> findTopByIndexInfoAndBaseDateLessThanEqualOrderByBaseDateDesc(
        IndexInfo info, LocalDate date);
}
