package com.findex.demo.syncJobs.mapper;

import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.dto.SyncJobDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SyncJobMapper {
  SyncJob toEntity(SyncJobDto dto);
  SyncJobDto toDto(SyncJob entity);
}
