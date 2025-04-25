package com.findex.demo.syncJobs.domain.dto;

import com.findex.demo.syncJobs.domain.type.JobType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncJobDto {
  private Integer id;

  private JobType jobType;

  private Integer indexInfoId;

  private LocalDate targetDate;

  private String worker;

  private LocalDate jobTime;

  private String result;
}