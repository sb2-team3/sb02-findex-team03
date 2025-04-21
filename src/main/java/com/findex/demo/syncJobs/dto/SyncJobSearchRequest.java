
package com.findex.demo.syncJobs.dto;

import com.findex.demo.syncJobs.type.JobType;
import com.findex.demo.syncJobs.type.Result;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncJobSearchRequest {

  private JobType jobType;
  private Long indexInfoId;
  private String baseDateFrom;
//  private Result result;
//  private Long lastId;
}
