package com.findex.demo.indexInfo.domain.dto;

import java.time.LocalDate;

public record IndexInfoUpdateRequest(
    int employedItemsCount,
    LocalDatetime basePointInTime,
    int baseIndex,
    boolean favorite
) {

}
