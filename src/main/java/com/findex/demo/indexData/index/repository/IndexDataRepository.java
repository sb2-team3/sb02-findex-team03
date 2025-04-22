package com.findex.demo.indexData.index.repository;

import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData,Integer >,IndexDataRepositoryCustom {
  public boolean existsByIndexInfoAndDate(IndexInfo indexInfo, LocalDate date);

}
