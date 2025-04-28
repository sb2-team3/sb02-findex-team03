package com.findex.demo.syncJobs.service;

import com.findex.demo.global.pagination.dto.PagedResponse;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.domain.dto.*;
import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.StatusType;
import com.findex.demo.syncJobs.mapper.SyncJobMapper;
import com.findex.demo.syncJobs.repository.SyncJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SyncJobService {

  private final SyncJobRepository syncJobRepository;

  public PagedResponse<SyncJobDto> searchSyncJobs(
      JobType jobType,
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
      int size
  ) {
    List<SyncJob> queryResult = syncJobRepository.findPage(
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
    ).fetch();

    Long pageTotalSize = syncJobRepository.countByConditions(jobType,
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

    if (queryResult == null) {
      queryResult = List.of();
    }

    boolean hasNext = false;
    String nextIdAfter = null;

    if (queryResult.size() > size) {
      hasNext = true;
      SyncJob lastItem = queryResult.get(size - 1);
      nextIdAfter = String.valueOf(lastItem.getId());
      queryResult = queryResult.subList(0, size);
    }

    List<SyncJobDto> content = queryResult.stream()
        .map(SyncJobMapper::toSyncJobDto)
        .collect(Collectors.toList());

    return new PagedResponse<>(
        content,
        null,
        nextIdAfter,
        size,
        pageTotalSize,
        hasNext
    );
  }
}
