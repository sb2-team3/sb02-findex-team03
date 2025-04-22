package com.findex.demo.indexData.datas.repository;

import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData, Integer> {

    List<IndexData> findByIndexInfoAndDateBetweenOrderByDateAsc(IndexInfo indexInfo,
        LocalDate startDate, LocalDate endDate);

    IndexData findByIndexInfoAndDateIn(IndexInfo indexInfo, Collection<LocalDate> date);

    IndexData findByIndexInfoIdAndDateBetween(Integer favoriteIndexId,
        LocalDate startDate, LocalDate endDate);

    List<IndexData> findByIndexInfoInAndDateBetween(List<IndexInfo> indexInfoList,
        LocalDate startDate, LocalDate endDate);
}
