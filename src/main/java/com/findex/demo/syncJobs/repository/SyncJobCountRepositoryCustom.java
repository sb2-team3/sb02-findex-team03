package com.findex.demo.syncJobs.repository;

import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.StatusType;
import java.time.LocalDate;

public interface SyncJobCountRepositoryCustom {


    Long countByConditions(JobType jobType, Integer indexInfoId, LocalDate baseDateFrom,
                  LocalDate baseDateTo, String worker, LocalDate jobTimeFrom, LocalDate jobTimeTo,
                  StatusType status, Integer idAfter, String cursor, String sortField,
                  String sortDirection, int size);

    }
