package com.findex.demo.syncJobs.api;

import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.StatusType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OpenApIIndexInfoMapper {

    public static IndexInfo toIndexInfo(ExternalIndexInfoDto externalIndexInfoDto) {
        return IndexInfo.builder()
            .indexName(externalIndexInfoDto.indexName())
            .baseIndex(externalIndexInfoDto.basId())
            .indexClassification(externalIndexInfoDto.indexClassification())
            .basePointInTime(parseBasePointInTime(externalIndexInfoDto.basePointInTimeRaw()))
            .employedItemCount(externalIndexInfoDto.employedItemCount())
            .sourceType(SourceType.OPEN_API)
            .build();
    }

    public static LocalDate parseBasePointInTime(String rawDate) {
        return LocalDate.parse(rawDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static SyncJob toSyncJob(IndexInfo info) {
        return SyncJob.builder()
            .jobType(JobType.INDEX_INFO)
            .statusType(StatusType.SUCCESS)
            .targetDate(info.getBasePointInTime())
            .jobTime(LocalDate.now())
            .worker("OpenAPI")
            .indexInfo(info)
            .build();
    }

    public static AutoSyncConfig toAutoSyncConfig(IndexInfo info, boolean enabled) {
        return AutoSyncConfig.builder()
            .indexInfo(info)
            .enabled(enabled)
            .build();
    }

}
