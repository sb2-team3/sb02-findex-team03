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

    // 필터 조건에 맞는 카운트를 계산하는 메서드
    @Query("SELECT COUNT(i) FROM IndexData i WHERE " +
        "i.indexInfo = :indexInfo AND " +
        "(i.baseDate BETWEEN :startDate AND :endDate)")
    long countWithFilter(
        IndexInfo indexInfo,
        LocalDate startDate,
        LocalDate endDate
    );

    boolean existsByBaseDate(LocalDate date);

    @Query("SELECT MAX(i.baseDate) FROM IndexData i WHERE i.baseDate < :date")
    Optional<LocalDate> findMaxBaseDateBeforeDate(@Param("date") LocalDate date);

    @Query("SELECT MIN(i.baseDate) FROM IndexData i WHERE i.baseDate > :date")
    Optional<LocalDate> findMinBaseDateAfterDate(@Param("date") LocalDate date);

    Optional<IndexData> findTopByIndexInfoAndBaseDateLessThanEqualOrderByBaseDateDesc(IndexInfo info, LocalDate date);

    Optional<IndexData> findFirstByIndexInfoAndBaseDateGreaterThanOrderByBaseDateAsc(IndexInfo indexInfo, LocalDate endDate);

    Optional<IndexData> findFirstByIndexInfoAndBaseDateLessThanOrderByBaseDateDesc(IndexInfo indexInfo, LocalDate startDate);

    void deleteByIndexInfo(IndexInfo indexInfo);
}