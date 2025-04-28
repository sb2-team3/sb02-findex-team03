package com.findex.demo.global.pagination.dto;
import java.util.List;

public record PagedResponse<T>(List<T> content, String nextCursor, String nextIdAfter, int size, int totalElements, Boolean hasNext) {

}
