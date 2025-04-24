package com.findex.demo.indexInfo.service.impl;

import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexInfo.domain.dto.CursorPageResponseIndexInfoDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoCreateRequest;
import com.findex.demo.indexInfo.domain.dto.IndexInfoDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoSummaryDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoUpdateRequest;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import com.findex.demo.indexInfo.mapper.IndexInfoMapper;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import com.findex.demo.indexInfo.service.IndexInfoService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexInfoServiceImpl implements IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;

  @Override
  @Transactional
  public IndexInfoDto create(IndexInfoCreateRequest createRequest) {
    String indexClassification = createRequest.indexClassification();
    String indexName = createRequest.indexName();

    if (indexInfoRepository.existsByIndexClassificationAndIndexName(indexClassification, indexName)) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다");
    }

    IndexInfo indexInfo = IndexInfoMapper.toEntity(createRequest, SourceType.USER);

    indexInfo = indexInfoRepository.save(indexInfo);

    return IndexInfoMapper.toIndexInfoDto(indexInfo);
  }

  @Override
  @Transactional
  public IndexInfoDto update(Integer id, IndexInfoUpdateRequest updateRequest) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다"));

    IndexInfoMapper.updateFromDto(updateRequest, indexInfo);

    indexInfoRepository.save(indexInfo);

    return IndexInfoMapper.toIndexInfoDto(indexInfo);
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다"));

    indexInfoRepository.delete(indexInfo);
  }

  @Override
  public IndexInfoDto getIndexInfo(Integer id) {
    return indexInfoRepository.findById(id)
        .map(IndexInfoMapper::toIndexInfoDto)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다"));
  }

  @Override
  public List<IndexInfoSummaryDto> getIndexInfoSummaries() {
    List<IndexInfo> indexInfos = indexInfoRepository.findAll();

    return indexInfos.stream()
        .map(IndexInfoMapper::toSummaryDto)
        .collect(Collectors.toList());
  }

  @Override
  public CursorPageResponseIndexInfoDto getIndexInfoList(
      String indexClassification,
      String indexName,
      Boolean favorite,
      Long idAfter,
      String cursor,
      String sortField,
      String sortDirection,
      int size) {

    // 정렬 방향 설정 (오름차순 또는 내림차순)
    boolean isAscending = "asc".equalsIgnoreCase(sortDirection);

    // cursor가 null이면 처음 페이지를 의미하므로 idAfter는 0으로 처리
    Long cursorValue = (idAfter != null) ? idAfter : (cursor != null ? Long.parseLong(cursor) : 0L);

    // Pageable 설정: size와 sortField, sortDirection을 기준으로 정렬
    Pageable pageable = PageRequest.of(0, size, Sort.by(
        isAscending ? Sort.Order.asc(sortField) : Sort.Order.desc(sortField)
    ));

    // Repository에서 동적 쿼리 실행
    Page<IndexInfo> pageResult = indexInfoRepository.findByFilter(
        indexClassification, indexName, favorite, cursorValue, pageable
    );

    // 엔티티를 DTO로 변환
    List<IndexInfoDto> indexInfoDtos = pageResult.getContent().stream()
        .map(IndexInfoMapper::toIndexInfoDto)
        .toList();

    boolean hasNext = pageResult.hasNext();  // 다음 페이지 여부 확인

    Integer nextCursor = null;
    if (!indexInfoDtos.isEmpty()) {
      IndexInfoDto lastItem = indexInfoDtos.get(indexInfoDtos.size() - 1);
      nextCursor = lastItem.id();  // 페이지 마지막 항목의 ID를 커서로 사용
    }

    long totalElements = 0;
    if (pageResult.hasContent()) {
      totalElements = indexInfoRepository.count();  // 전체 데이터 수
    }

    // 커서 기반 페이징을 위한 반환 값
    return new CursorPageResponseIndexInfoDto(
        new ArrayList<>(indexInfoDtos),
        nextCursor,
        nextCursor != null ? nextCursor.longValue() : null,
        size,
        totalElements,
        hasNext
    );
  }
}