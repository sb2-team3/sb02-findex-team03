package com.findex.demo.syncJobs.cursor;

import com.findex.demo.syncJobs.domain.entity.QSyncJob;
import com.querydsl.core.types.OrderSpecifier;

public class CursorOrder {

    public static OrderSpecifier<?> order(QSyncJob job, String sortField, String sortDirection) {
        if ("jobTime".equals(sortField)) {
            if ("asc".equalsIgnoreCase(sortDirection)) {
                return job.jobTime.asc();
            } else {
                return job.jobTime.desc();
            }
        } else if ("targetDate".equals(sortField)) {
            if ("asc".equalsIgnoreCase(sortDirection)) {
                return job.targetDate.asc();
            } else {
                return job.targetDate.desc();
            }
        }
        return job.jobTime.desc();
    }
}
