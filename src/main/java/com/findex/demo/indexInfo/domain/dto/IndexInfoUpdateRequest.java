package com.findex.demo.indexInfo.domain.dto;

import java.time.Instant;

public record IndexInfoUpdateRequest(
    int employedItemCount,
    Instant basePointInTime,
    double baseIndex,
    boolean favorite
) {

}