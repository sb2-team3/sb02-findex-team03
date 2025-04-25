package com.findex.demo.syncJobs.service;

import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.domain.dto.*;
import com.findex.demo.syncJobs.mapper.SyncJobMapper;
import com.findex.demo.syncJobs.repository.SyncJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SyncJobService {

  private final SyncJobRepository syncJobRepository;

  public CursorPageResponseSyncJobDto<SyncJobDto> searchSyncJobs(SyncJobSearchRequest request, String sortField, String sortDirection, Long idAfter, int size) {
    Pageable pageable = PageRequest.of(0, size, getSort(sortField, sortDirection));

    Page<SyncJob> page = syncJobRepository.findByFilter(
        request.getJobType(),
        request.getIndexInfoId() != null ? request.getIndexInfoId().intValue() : null,
        parseLocalDate(request.getBaseDateFrom()),
        parseLocalDate(request.getBaseDateTo()),
        request.getWorker(),
        parseLocalDateTime(request.getJobTimeFrom()),
        parseLocalDateTime(request.getJobTimeTo()),
        request.getResult(),
        idAfter,
        pageable
    );

    List<SyncJobDto> dtos = page.getContent().stream()
        .map(SyncJobMapper::toDto)
        .collect(Collectors.toList());

    Long nextIdAfter = dtos.isEmpty() ? null : dtos.get(dtos.size() - 1).getId().longValue();

    return CursorPageResponseSyncJobDto.<SyncJobDto>builder()
        .content(dtos)
        .nextCursor(nextIdAfter != null ? String.valueOf(nextIdAfter) : null)
        .nextIdAfter(nextIdAfter)
        .size(size)
        .totalElements(page.getTotalElements())
        .hasNext(page.hasNext())
        .build();
  }

  private Sort getSort(String sortField, String direction) {
    if (!StringUtils.hasText(sortField)) sortField = "jobTime"; // 기본 정렬
    Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
    return Sort.by(dir, sortField);
  }

  private LocalDate parseLocalDate(String str) {
    return StringUtils.hasText(str) ? LocalDate.parse(str) : null;
  }

  private LocalDateTime parseLocalDateTime(String str) {
    return StringUtils.hasText(str) ? LocalDateTime.parse(str) : null;
  }
}
