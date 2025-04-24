package com.findex.demo.indexData.index.repository;

import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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


    List<IndexData> findByIndexInfoIdInAndBaseDateIn(List<Integer> favoriteIndexIds,
        List<LocalDate> startDate);

    boolean existsByBaseDate(LocalDate date);

    @Query("SELECT MAX(i.baseDate) FROM IndexData i WHERE i.baseDate < :date")
    Optional<LocalDate> findMaxBaseDateBeforeDate(@Param("date") LocalDate date);

    @Query("SELECT MIN(i.baseDate) FROM IndexData i WHERE i.baseDate > :date")
    Optional<LocalDate> findMinBaseDateAfterDate(@Param("date") LocalDate date);

    Optional<IndexData> findTopByIndexInfoAndBaseDateLessThanEqualOrderByBaseDateDesc(
        IndexInfo info, LocalDate date);
}
