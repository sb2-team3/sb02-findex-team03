package com.findex.demo.syncJobs.api;

import lombok.Builder;

@Builder
public record ExternalIndexInfoDto(String indexName,                // idxNm
                                   String indexClassification,      // idxCsf
                                   Integer employedItemCount,           // epyItmsCnt
                                   Integer basId,
                                   String basePointInTimeRaw      // basPntm (yyyyMMdd)
) {
}
