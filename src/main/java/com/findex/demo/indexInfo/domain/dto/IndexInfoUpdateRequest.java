package com.findex.demo.indexInfo.domain.dto;

import java.time.LocalDate;

public record IndexInfoUpdateRequest(
    int employedItemsCount,
    LocalDate basePointInTime,
    int baseIndex,
    Boolean favorite
) {

}