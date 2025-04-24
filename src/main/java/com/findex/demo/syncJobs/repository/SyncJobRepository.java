package com.findex.demo.syncJobs.repository;

import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.type.JobType;
import com.findex.demo.syncJobs.type.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SyncJobRepository extends JpaRepository<SyncJob, Integer> {

  @Query("SELECT s FROM SyncJob s WHERE " +
      "(:jobType IS NULL OR s.jobType = :jobType) AND " +
      "(:indexInfoId IS NULL OR s.indexInfo.id = :indexInfoId) AND " +
      "(:targetDateFrom IS NULL OR s.targetDate >= :targetDateFrom) AND " +
      "(:targetDateTo IS NULL OR s.targetDate <= :targetDateTo) AND " +
      "(:worker IS NULL OR s.worker LIKE %:worker%) AND " +
      "(:jobTimeFrom IS NULL OR s.jobTime >= :jobTimeFrom) AND " +
      "(:jobTimeTo IS NULL OR s.jobTime <= :jobTimeTo) AND " +
      "(:result IS NULL OR s.result = :result) AND " +
      "(:idAfter IS NULL OR s.id > :idAfter)")
  Page<SyncJob> findByFilter(
      @Param("jobType") JobType jobType,
      @Param("indexInfoId") Integer indexInfoId,
      @Param("targetDateFrom") LocalDate targetDateFrom,
      @Param("targetDateTo") LocalDate targetDateTo,
      @Param("worker") String worker,
      @Param("jobTimeFrom") LocalDateTime jobTimeFrom,
      @Param("jobTimeTo") LocalDateTime jobTimeTo,
      @Param("result") Result result,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );
}
