package com.findex.demo.syncJobs.api;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
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


}
