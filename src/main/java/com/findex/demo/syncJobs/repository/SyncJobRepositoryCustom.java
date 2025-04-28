package com.findex.demo.syncJobs.repository;

import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.StatusType;
import com.querydsl.jpa.impl.JPAQuery;
import java.time.LocalDate;

public interface SyncJobRepositoryCustom {

    JPAQuery<SyncJob> findPage(JobType jobType,
                               Integer indexInfoId,
                               LocalDate baseDateFrom,
                               LocalDate baseDateTo,
                               String worker,
                               LocalDate jobTimeFrom,
                               LocalDate jobTimeTo,
                               StatusType status,
                               Integer idAfter,
                               String cursor,
                               String sortField,
                               String sortDirection,
                               int size);
}
