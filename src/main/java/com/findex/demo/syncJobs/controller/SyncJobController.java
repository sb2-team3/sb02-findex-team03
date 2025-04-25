package com.findex.demo.syncJobs.controller;

import com.findex.demo.global.pagination.dto.PagedResponse;
import com.findex.demo.syncJobs.domain.dto.SyncJobDto;
import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.StatusType;
import com.findex.demo.syncJobs.service.SyncJobService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync-jobs")
@RequiredArgsConstructor
public class SyncJobController {

    private final SyncJobService syncJobService;

    @GetMapping
    public ResponseEntity<PagedResponse<SyncJobDto>> searchSyncJobs(
            @RequestParam(defaultValue = "INDEX_INFO", required = false)
            JobType jobType,
            @RequestParam(required = false)
            Integer indexInfoId,

            @RequestParam(required = false)
            LocalDate baseDateFrom,

            @RequestParam(required = false)
            LocalDate baseDateTo,

            @RequestParam(required = false)
            String worker,

            @RequestParam(required = false)
            LocalDate jobTimeFrom,

            @RequestParam(required = false)
            LocalDate jobTimeTo,

            @RequestParam(required = false)
            StatusType status,

            @RequestParam(required = false)
            Integer idAfter,

            @RequestParam(required = false)
            String cursor,

            @RequestParam(defaultValue = "jobTime")
            String sortField,

            @RequestParam(defaultValue = "desc")
            String sortDirection,

            @RequestParam(defaultValue = "10")
            int size
    ) {
        PagedResponse<SyncJobDto> response = syncJobService.searchSyncJobs(
                jobType,
                indexInfoId,
                baseDateFrom,
                baseDateTo,
                worker,
                jobTimeFrom,
                jobTimeTo,
                status,
                idAfter,
                cursor,
                sortField,
                sortDirection,
                size
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
