package com.findex.demo.syncJobs.api;

public record ExternalIndexInfoDto(  String indexName,                // idxNm
                                     String indexClassification,      // idxCsf
                                     int employedItemCount,           // epyItmsCnt
                                     String basePointInTimeRaw      // basPntm (yyyyMMdd)
) {
}
