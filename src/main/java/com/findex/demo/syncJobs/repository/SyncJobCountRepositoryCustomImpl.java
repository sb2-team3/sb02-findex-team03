package com.findex.demo.syncJobs.repository;

import com.findex.demo.syncJobs.cursor.CursorWhere;
import com.findex.demo.syncJobs.domain.entity.QSyncJob;
import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.StatusType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SyncJobCountRepositoryCustomImpl implements SyncJobCountRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countByConditions(JobType jobType, Integer indexInfoId, LocalDate baseDateFrom,
                          LocalDate baseDateTo, String worker, LocalDate jobTimeFrom, LocalDate jobTimeTo,
                          StatusType status, Integer idAfter, String cursor, String sortField,
                          String sortDirection, int size) {

        QSyncJob syncJob = QSyncJob.syncJob;
        BooleanBuilder where = CursorWhere.booleanBuilder(syncJob, jobType, indexInfoId, baseDateFrom,
                baseDateTo, worker, jobTimeFrom, jobTimeTo, status, idAfter);


        Long totalElements = queryFactory
                .select(syncJob.count())
                .from(syncJob)
                .where(where)
                .fetchOne();

        return totalElements;
    }
}
