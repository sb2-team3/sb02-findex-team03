package com.findex.demo.syncJobs.mapper;

import com.findex.demo.syncJobs.domain.dto.SyncJobDto;
import com.findex.demo.syncJobs.domain.entity.SyncJob;

import java.time.ZoneId;

public class SyncJobMapper {

  public static SyncJobDto toDto(SyncJob entity) {
    if (entity == null) return null;

    return SyncJobDto.builder()
        .id(entity.getId())
        .jobType(entity.getJobType())
        .indexInfoId(entity.getIndexInfo().getId())
        .targetDate(entity.getTargetDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
        .worker(entity.getWorker())
        .jobTime(entity.getJobTime().atZone(ZoneId.systemDefault()).toInstant())
        .result(entity.getResult())
        .build();
  }

}
