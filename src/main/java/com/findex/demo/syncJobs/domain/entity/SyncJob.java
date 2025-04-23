package com.findex.demo.syncJobs.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "sync_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncJob {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private JobType jobType;

  @Column(nullable = false)
  private Long indexInfoId;

  @Column(nullable = false)
  private Instant targetDate;

  @Column(nullable = false)
  private String worker;

  @Column(nullable = false)
  private Instant jobTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Result result;

  public enum JobType {
    INDEX_INFO, INDEX_DATA
  }

  public enum Result {
    SUCCESS, FAILURE
  }
}
