package com.findex.demo.syncJobs.domain.dto;

import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.Result;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncJobSearchRequest {

  private JobType jobType;
  private Long indexInfoId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate baseDateFrom;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate baseDateTo;

  private String worker;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime jobTimeFrom;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime jobTimeTo;

}
