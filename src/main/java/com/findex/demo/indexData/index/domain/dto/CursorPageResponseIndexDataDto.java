package com.findex.demo.indexData.index.domain.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CursorPageResponseIndexDataDto<IndexDataDto> {
  List<IndexDataDto> contents;
  String nextCursor;
  Integer nextIdAfter;
  Integer size;
  Integer totalElements;
  boolean hashNext;
}
