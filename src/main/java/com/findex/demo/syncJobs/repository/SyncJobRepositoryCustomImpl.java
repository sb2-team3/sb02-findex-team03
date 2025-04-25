package com.findex.demo.syncJobs.repository;

import com.findex.demo.syncJobs.cursor.CursorOrder;
import com.findex.demo.syncJobs.cursor.CursorWhere;
import com.findex.demo.syncJobs.domain.entity.QSyncJob;
import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.StatusType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SyncJobRepositoryCustomImpl implements SyncJobRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public JPAQuery<SyncJob> findPage(JobType jobType, Integer indexInfoId, LocalDate baseDateFrom, LocalDate baseDateTo,
                                      String worker, LocalDate jobTimeFrom, LocalDate jobTimeTo, StatusType status,
                                      Integer idAfter, String cursor, String sortField, String sortDirection, int size) {

        QSyncJob syncJob = QSyncJob.syncJob;
        BooleanBuilder where = CursorWhere.booleanBuilder(syncJob, jobType, indexInfoId, baseDateFrom,
                baseDateTo, worker, jobTimeFrom, jobTimeTo, status, idAfter);
        OrderSpecifier<?> order = CursorOrder.order(syncJob, sortField, sortDirection);

        JPAQuery<SyncJob> syncJobJPAQuery = queryFactory.selectFrom(syncJob)
                .where(where)
                .orderBy(order)
                .limit(size + 1);

        return syncJobJPAQuery;
    }
}
