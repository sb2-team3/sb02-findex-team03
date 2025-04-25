package com.findex.demo.syncJobs.mapper;

import com.findex.demo.syncJobs.domain.dto.SyncJobDto;
import com.findex.demo.syncJobs.domain.entity.SyncJob;

public class SyncJobMapper {

  public static SyncJobDto toSyncJobDto(SyncJob entity) {
    return SyncJobDto.builder()
        .id(entity.getId())
        .jobType(entity.getJobType())
        .indexInfoId(entity.getIndexInfo().getId())
        .targetDate(entity.getTargetDate())
        .worker(entity.getWorker())
        .jobTime(entity.getJobTime())
        .result("NEW")
        .build();
  }

}
