package com.findex.demo.syncJobs.repository;

import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.type.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SyncJobRepository extends JpaRepository<SyncJob, Integer> {

  @Query("SELECT s FROM SyncJob s WHERE " +
      "(:jobType IS NULL OR s.jobType = :jobType) AND " +
      "(:indexInfoId IS NULL OR s.indexInfo.id = :indexInfoId) AND " +
      "(:targetDateFrom IS NULL OR s.targetDate >= :targetDateFrom) AND " +
      "(:targetDateTo IS NULL OR s.targetDate <= :targetDateTo) AND " +
      "(:worker IS NULL OR s.worker LIKE CONCAT('%', :worker, '%')) AND " +
      "(:jobTimeFrom IS NULL OR s.jobTime >= :jobTimeFrom) AND " +
      "(:jobTimeTo IS NULL OR s.jobTime <= :jobTimeTo) " +
      "ORDER BY s.jobTime DESC, s.id ASC")
  List<SyncJob> findFirstPageByJobTime(
      @Param("jobType") JobType jobType,
      @Param("indexInfoId") Integer indexInfoId,
      @Param("targetDateFrom") LocalDate targetDateFrom,
      @Param("targetDateTo") LocalDate targetDateTo,
      @Param("worker") String worker,
      @Param("jobTimeFrom") LocalDateTime jobTimeFrom,
      @Param("jobTimeTo") LocalDateTime jobTimeTo
  );

  @Query("SELECT s FROM SyncJob s WHERE " +
      "(:jobType IS NULL OR s.jobType = :jobType) AND " +
      "(:indexInfoId IS NULL OR s.indexInfo.id = :indexInfoId) AND " +
      "(:targetDateFrom IS NULL OR s.targetDate >= :targetDateFrom) AND " +
      "(:targetDateTo IS NULL OR s.targetDate <= :targetDateTo) AND " +
      "(:worker IS NULL OR s.worker LIKE CONCAT('%', :worker, '%')) AND " +
      "(:jobTimeFrom IS NULL OR s.jobTime >= :jobTimeFrom) AND " +
      "(:jobTimeTo IS NULL OR s.jobTime <= :jobTimeTo) AND " +
      "s.id > :idAfter " +
      "ORDER BY s.jobTime DESC, s.id ASC")
  List<SyncJob> findByJobTimeAfterCursor(
      @Param("jobType") JobType jobType,
      @Param("indexInfoId") Integer indexInfoId,
      @Param("targetDateFrom") LocalDate targetDateFrom,
      @Param("targetDateTo") LocalDate targetDateTo,
      @Param("worker") String worker,
      @Param("jobTimeFrom") LocalDateTime jobTimeFrom,
      @Param("jobTimeTo") LocalDateTime jobTimeTo,
      @Param("idAfter") Long idAfter
  );

  @Query("SELECT s FROM SyncJob s WHERE " +
      "(:jobType IS NULL OR s.jobType = :jobType) AND " +
      "(:indexInfoId IS NULL OR s.indexInfo.id = :indexInfoId) AND " +
      "(:targetDateFrom IS NULL OR s.targetDate >= :targetDateFrom) AND " +
      "(:targetDateTo IS NULL OR s.targetDate <= :targetDateTo) AND " +
      "(:worker IS NULL OR s.worker LIKE CONCAT('%', :worker, '%')) AND " +
      "(:jobTimeFrom IS NULL OR s.jobTime >= :jobTimeFrom) AND " +
      "(:jobTimeTo IS NULL OR s.jobTime <= :jobTimeTo) " +
      "ORDER BY s.targetDate DESC, s.id ASC")
  List<SyncJob> findFirstPageByTargetDate(
      @Param("jobType") JobType jobType,
      @Param("indexInfoId") Integer indexInfoId,
      @Param("targetDateFrom") LocalDate targetDateFrom,
      @Param("targetDateTo") LocalDate targetDateTo,
      @Param("worker") String worker,
      @Param("jobTimeFrom") LocalDateTime jobTimeFrom,
      @Param("jobTimeTo") LocalDateTime jobTimeTo
  );

  @Query("SELECT s FROM SyncJob s WHERE " +
      "(:jobType IS NULL OR s.jobType = :jobType) AND " +
      "(:indexInfoId IS NULL OR s.indexInfo.id = :indexInfoId) AND " +
      "(:targetDateFrom IS NULL OR s.targetDate >= :targetDateFrom) AND " +
      "(:targetDateTo IS NULL OR s.targetDate <= :targetDateTo) AND " +
      "(:worker IS NULL OR s.worker LIKE CONCAT('%', :worker, '%')) AND " +
      "(:jobTimeFrom IS NULL OR s.jobTime >= :jobTimeFrom) AND " +
      "(:jobTimeTo IS NULL OR s.jobTime <= :jobTimeTo) AND " +
      "s.id > :idAfter " +
      "ORDER BY s.targetDate DESC, s.id ASC")
  List<SyncJob> findByTargetDateAfterCursor(
      @Param("jobType") JobType jobType,
      @Param("indexInfoId") Integer indexInfoId,
      @Param("targetDateFrom") LocalDate targetDateFrom,
      @Param("targetDateTo") LocalDate targetDateTo,
      @Param("worker") String worker,
      @Param("jobTimeFrom") LocalDateTime jobTimeFrom,
      @Param("jobTimeTo") LocalDateTime jobTimeTo,
      @Param("idAfter") Long idAfter
  );
}
