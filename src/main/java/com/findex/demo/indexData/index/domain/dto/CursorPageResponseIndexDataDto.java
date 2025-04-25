package com.findex.demo.indexData.index.domain.dto;

import jakarta.validation.constraints.NotNull;
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
  @NotNull(message = "no cursor")
  String nextCursor;
  Integer nextIdAfter;
  Integer size;
  Long totalElements;

  boolean hasNext;
}
