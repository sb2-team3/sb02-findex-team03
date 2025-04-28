package com.findex.demo.syncJobs.mapper;

import com.findex.demo.syncJobs.domain.dto.SyncJobDto;
import com.findex.demo.syncJobs.domain.entity.SyncJob;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SyncJobMapper {

  public static SyncJobDto toSyncJobDto(SyncJob entity) {
    return SyncJobDto.builder()
        .id(entity.getId())
        .jobType(entity.getJobType())
        .indexInfoId(entity.getIndexInfo().getId())
        .targetDate(entity.getTargetDate())
        .worker(entity.getWorker())
        .jobTime(LocalDateTime.now())
        .result(entity.getStatusType().name())
        .build();
  }

}
