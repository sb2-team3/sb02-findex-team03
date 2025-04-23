package com.findex.demo.indexInfo.domain.dto;

import java.util.List;

public record CursorPageResponseIndexInfoDto(
    List<Object> content,
    String nextCursor,
    Long nextIdAfter,
    Integer size,
    Long totalElements,
    Boolean hasNext
) {

}
