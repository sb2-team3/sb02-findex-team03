package com.findex.demo.syncJobs.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiSyncResultResponse {

  private int createdCount;
  private int updatedCount;
  private int skippedCount;
  private int totalCount;
}
