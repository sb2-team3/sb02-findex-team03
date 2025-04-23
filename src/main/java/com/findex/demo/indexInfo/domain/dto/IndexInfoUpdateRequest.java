package com.findex.demo.indexInfo.domain.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record IndexInfoUpdateRequest(
    int employedItemsCount,
    LocalDate basePointInTime,
    int baseIndex,
    boolean favorite
) {

}