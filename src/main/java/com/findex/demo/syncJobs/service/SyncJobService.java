package com.findex.demo.syncJobs.service;

import com.findex.demo.syncJobs.domain.dto.*;
import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.mapper.SyncJobMapper;
import com.findex.demo.syncJobs.repository.SyncJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SyncJobService {

  private final SyncJobRepository syncJobRepository;

  public CursorPageResponseSyncJobDto<SyncJobDto> searchSyncJobs(
      SyncJobSearchRequest request,
      String sortField,
      String sortDirection,
      Long idAfter,
      int size
  ) {
    boolean sortByTargetDate = "targetDate".equalsIgnoreCase(sortField);
    List<SyncJob> jobs;

    if (sortByTargetDate) {
      jobs = (idAfter == null)
          ? syncJobRepository.findFirstPageByTargetDate(
          request.getJobType(),
          request.getIndexInfoId() != null ? request.getIndexInfoId().intValue() : null,
          request.getBaseDateFrom(),
          request.getBaseDateTo(),
          request.getWorker(),
          request.getJobTimeFrom(),
          request.getJobTimeTo()
      )
          : syncJobRepository.findByTargetDateAfterCursor(
              request.getJobType(),
              request.getIndexInfoId() != null ? request.getIndexInfoId().intValue() : null,
              request.getBaseDateFrom(),
              request.getBaseDateTo(),
              request.getWorker(),
              request.getJobTimeFrom(),
              request.getJobTimeTo(),
              idAfter
          );
    } else {
      jobs = (idAfter == null)
          ? syncJobRepository.findFirstPageByJobTime(
          request.getJobType(),
          request.getIndexInfoId() != null ? request.getIndexInfoId().intValue() : null,
          request.getBaseDateFrom(),
          request.getBaseDateTo(),
          request.getWorker(),
          request.getJobTimeFrom(),
          request.getJobTimeTo()
      )
          : syncJobRepository.findByJobTimeAfterCursor(
              request.getJobType(),
              request.getIndexInfoId() != null ? request.getIndexInfoId().intValue() : null,
              request.getBaseDateFrom(),
              request.getBaseDateTo(),
              request.getWorker(),
              request.getJobTimeFrom(),
              request.getJobTimeTo(),
              idAfter
          );
    }

    List<SyncJobDto> dtos = jobs.stream()
        .limit(size)
        .map(SyncJobMapper::toDto)
        .collect(Collectors.toList());

    Long nextIdAfter = dtos.isEmpty() ? null : dtos.get(dtos.size() - 1).getId().longValue();

    return CursorPageResponseSyncJobDto.<SyncJobDto>builder()
        .content(dtos)
        .nextCursor(nextIdAfter != null ? String.valueOf(nextIdAfter) : null)
        .nextIdAfter(nextIdAfter)
        .size(size)
        .totalElements((long) dtos.size())
        .hasNext(dtos.size() == size)
        .build();
  }
}
