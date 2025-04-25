package com.findex.demo.syncJobs.controller;

import com.findex.demo.syncJobs.domain.dto.CursorPageResponseSyncJobDto;
import com.findex.demo.syncJobs.domain.dto.SyncJobDto;
import com.findex.demo.syncJobs.domain.dto.SyncJobSearchRequest;
import com.findex.demo.syncJobs.service.SyncJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync-jobs")
@RequiredArgsConstructor
public class SyncJobController {

  private final SyncJobService syncJobService;

  @GetMapping
  public CursorPageResponseSyncJobDto<SyncJobDto> searchSyncJobs(
      @ModelAttribute SyncJobSearchRequest request,
      @RequestParam(defaultValue = "jobTime") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(defaultValue = "10") int size
  ) {
    return syncJobService.searchSyncJobs(request, sortField, sortDirection, idAfter, size);
  }
}
