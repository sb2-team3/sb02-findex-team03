package com.findex.demo.syncJobs.cursor;

import com.findex.demo.syncJobs.domain.entity.QSyncJob;
import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.StatusType;
import com.querydsl.core.BooleanBuilder;
import java.time.LocalDate;

public class CursorWhere {

    public static BooleanBuilder booleanBuilder(QSyncJob qSyncJob, JobType jobType,
        Integer indexInfoId,
        LocalDate baseDateFrom,
        LocalDate baseDateTo,
        String worker,
        LocalDate jobTimeFrom,
        LocalDate jobTimeTo,
        StatusType status,
        Integer idAfter) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (jobType != null) {
            if (JobType.ALL.equals(jobType)) {
                booleanBuilder.and(
                        qSyncJob.jobType.in(JobType.INDEX_INFO, JobType.INDEX_DATA)
                );
            } else {
                booleanBuilder.and(
                        qSyncJob.jobType.eq(jobType)
                );
            }
        }


        if (indexInfoId != null) {
            booleanBuilder.and(qSyncJob.indexInfo.id.eq(indexInfoId));
        }

        if (baseDateFrom != null && baseDateTo != null) {
            booleanBuilder.and(qSyncJob.jobTime.between(baseDateFrom, baseDateTo));
        } else if (baseDateFrom != null) {
            booleanBuilder.and(qSyncJob.jobTime.goe(baseDateFrom));
        } else if (baseDateTo != null) {
            booleanBuilder.and(qSyncJob.jobTime.loe(baseDateTo));
        }

        if (worker != null) {
            booleanBuilder.and(qSyncJob.worker.eq(worker));
        }

        if (jobTimeFrom != null && jobTimeTo != null) {
            booleanBuilder.and(qSyncJob.jobTime.between(jobTimeFrom, jobTimeTo));
        } else if (jobTimeFrom != null) {
            booleanBuilder.and(qSyncJob.jobTime.goe(jobTimeFrom));
        } else if (jobTimeTo != null) {
            booleanBuilder.and(qSyncJob.jobTime.loe(jobTimeTo));
        }

        if (status != null) {
            booleanBuilder.and(qSyncJob.statusType.eq(status));
        }

        if (idAfter != null) {
            booleanBuilder.and(qSyncJob.id.gt(idAfter));
        }

        return booleanBuilder;
    }
}
