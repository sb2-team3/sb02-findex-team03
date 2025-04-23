package com.findex.demo.syncJobs.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IndexDataSyncRequestDto {
  private List<Integer> indexInfoIds;
  private String baseDateFrom;
  private String baseDateTo;
}
