package com.findex.demo.syncJobs.repository;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.syncJobs.domain.entity.SyncJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncJobRepository extends JpaRepository<SyncJob, Integer>, SyncJobRepositoryCustom,
        SyncJobCountRepositoryCustom {

  void deleteByIndexInfo(IndexInfo indexInfo);
}
