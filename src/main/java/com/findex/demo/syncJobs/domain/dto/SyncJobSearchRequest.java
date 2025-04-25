package com.findex.demo.syncJobs.domain.dto;

import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.StatusType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncJobSearchRequest {

  private JobType jobType;
  private Long indexInfoId;
  private String baseDateFrom;
  private String baseDateTo;
  private String worker;
  private String jobTimeFrom;
  private String jobTimeTo;
  private StatusType status;
}
