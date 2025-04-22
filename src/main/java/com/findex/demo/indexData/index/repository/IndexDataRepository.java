package com.findex.demo.indexData.index.repository;

import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData,Integer >,IndexDataRepositoryCustom {

  @Query("SELECT COUNT(i) > 0 FROM IndexData i WHERE i.indexInfo = :indexInfo AND i.baseDate = :baseDate")
  boolean existsByIndexInfoAndBaseDate(@Param("indexInfo") IndexInfo indexInfo,
                                       @Param("baseDate") LocalDate baseDate);

}
