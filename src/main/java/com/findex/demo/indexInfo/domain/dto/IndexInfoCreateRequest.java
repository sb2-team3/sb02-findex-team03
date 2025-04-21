package com.findex.demo.indexInfo.domain.dto;

import java.time.Instant;

public record IndexInfoCreateRequest(
    String indexClassification,
    String indexName,
    int employedItemCount,
    Instant basePointInTime,
    double baseIndex,
    boolean favorite
) {

}
