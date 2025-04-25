package com.findex.demo.indexData.index.domain.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CursorPageResponseIndexDataDto<IndexDataDto> {
  List<IndexDataDto> content;
  String nextCursor;
  Integer nextIdAfter;
  Integer size;
  Integer totalElements;
  boolean hasNext;
}
