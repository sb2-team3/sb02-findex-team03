package com.findex.demo.syncJobs.repository;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.syncJobs.domain.entity.SyncJob;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SyncJobRepository extends JpaRepository<SyncJob, Integer>, SyncJobRepositoryCustom,
        SyncJobCountRepositoryCustom {

  void deleteByIndexInfo(IndexInfo indexInfo);

  @Query("SELECT MAX(s.jobTime) FROM SyncJob s WHERE s.indexInfo.id = :indexInfoId")
  LocalDate findLastSuccessSyncTime(@Param("indexInfoId") Integer indexInfoId);

}
