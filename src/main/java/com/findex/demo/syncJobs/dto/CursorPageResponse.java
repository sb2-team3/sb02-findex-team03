package com.findex.demo.syncJobs.dto;

import lombok.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursorPageResponse<T> {

  private List<T> content;
  private String nextCursor;
  private Long nextIdAfter;
  private Integer size;
  private Long totalElements;
  private Boolean hasNext;
}
