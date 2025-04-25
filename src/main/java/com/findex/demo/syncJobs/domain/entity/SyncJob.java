package com.findex.demo.syncJobs.domain.entity;

import com.findex.demo.global.times.BaseTimeEntity;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.StatusType;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;


@Entity
@Table(name = "sync_jobs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncJob extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "index_info_id")
  private IndexInfo indexInfo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private JobType jobType;

  @Column(nullable = false)
  private LocalDate targetDate;

  @Column(nullable = false)
  private String worker;

  @Column(nullable = false)
  private LocalDate jobTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StatusType statusType;



}
