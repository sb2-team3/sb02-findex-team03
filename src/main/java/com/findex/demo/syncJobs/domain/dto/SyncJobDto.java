package com.findex.demo.syncJobs.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.findex.demo.syncJobs.type.JobType;
import com.findex.demo.syncJobs.type.Result;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncJobDto {
  private Integer id;

  private JobType jobType;

  private Integer indexInfoId;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Instant targetDate;

  private String worker;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private Instant jobTime;

  private Result result;
}