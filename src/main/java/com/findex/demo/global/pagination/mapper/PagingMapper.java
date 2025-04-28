package com.findex.demo.global.pagination.mapper;

import com.findex.demo.global.pagination.dto.PagedResponse;
import java.util.List;

public class PagingMapper {

    public static <T> PagedResponse<T> toPageResponse(
            List<T> content, String nextCursor, String nextIdAfter, int size, int totalElements, Boolean hasNext
    ) {
        return new PagedResponse<>(
                content,
                nextCursor,
                nextIdAfter,
                size,
                (long) totalElements,
                hasNext
        );
    }

}
