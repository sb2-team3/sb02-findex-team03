package com.findex.demo.indexInfo.domain.dto;

import java.time.LocalDateTime;

public record IndexInfoUpdateRequest(
    int employedItemsCount,
    LocalDateTime basePointInTime,
    int baseIndex,
    boolean favorite
) {

}
