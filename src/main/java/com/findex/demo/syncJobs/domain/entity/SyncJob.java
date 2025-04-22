package com.findex.demo.syncJobs.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @Column(nullable = false)
    private Long indexInfoId;

    @Column(nullable = false)
    private LocalDateTime targetDate;

    @Column(nullable = false)
    private String worker;

    @Column(nullable = false)
    private LocalDateTime jobTime;

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
