package com.findex.demo.indexData.index.service;

import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexData.index.domain.dto.CursorPageResponseIndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.dto.IndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataSearchCondition;
import com.findex.demo.indexData.index.domain.dto.IndexDataUpdateRequest;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexData.index.mapper.IndexDataMapper;
import com.findex.demo.indexData.index.repository.IndexDataRepository;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;

import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.awt.print.Pageable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndexDataService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexDataRepository indexDataRepository;

//  @Transactional
//  public CursorPageResponseIndexDataDto findAll(IndexDataSearchCondition condition) {
//
//    // 커서 기반 처리 기준 설정
//    Pageable pageable = (Pageable) PageRequest.of(
//        0,
//        condition.getSize(),
//        Sort.by(Direction.fromString(condition.getSortDirection()), condition.getSortField())
//    );
//
//    // Querydsl 또는 JPA Criteria를 활용한 필터링, 커서 기반 쿼리 수행
//    List<IndexData> results = indexDataRepository.findByCondition(condition, pageable);
//
//    // 다음 커서 계산 (마지막 요소 ID 기준)
//    String nextCursor = null;
//    Integer nextIdAfter = null;
//    boolean hashNext = false;
//
//    if (!results.isEmpty()) {
//      IndexData last = results.get(results.size() - 1);
//      nextCursor = encodeCursor(last.getId());
//      nextIdAfter = last.getId();
//      hashNext = results.size() == condition.getSize();
//    }
//
//    IndexDataMapper mapper = new IndexDataMapper();
//    // DTO 변환
//    List<IndexDataDto> content = results.stream()
//        .map(data->{return mapper.toDto(data);})
//        .toList();
//    ;
//
//    return CursorPageResponseIndexDataDto.<IndexDataDto>builder()
//        .contents(content)
//        .nextCursor(nextCursor)
//        .nextIdAfter(nextIdAfter)
//        .size(condition.getSize())
//        .totalElements(indexDataRepository.countByCondition(condition)) // 또는 null
//        .hashNext(hashNext)
//        .build();
//  }


  @Transactional
  public IndexDataDto create(IndexDataCreateRequest request) {

    // 1. 해당 지수 정보 존재 여부 확인
   IndexInfo indexInfo = indexInfoRepository.findById(request.getIndexInfoId())
       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE,"부서 코드는 필수 입니다."));

    // 2. 중복 확인
    boolean exists = indexDataRepository.existsByIndexInfoAndDate(indexInfo, request.getBaseDate());
    if (exists) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE,"부서 코드는 필수 입니다.");
    }

    // 3. 등록
    IndexData indexData = IndexDataMapper.toIndexData(request, indexInfo);
    indexDataRepository.save(indexData);

    return IndexDataMapper.toIndexDto(indexData);
  }

  public IndexDataDto update(Integer id, IndexDataUpdateRequest request) {
    IndexData indexData = indexDataRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE,"부서 코드는 필수 입니다"));

    indexData.update(request);

    IndexData updated = indexDataRepository.save(indexData);

    IndexDataMapper mapper = new IndexDataMapper();

    return mapper.toDto(updated);
  }

  /**
   * 지수 데이터 삭제
   * @param id IndexData ID
   */
  public void delete(Integer id) {
    IndexData indexData = indexDataRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE,"부서코드는 필수 입니다."));
    indexDataRepository.delete(indexData);
  }
  private String encodeCursor(Integer id) {
    return Base64.getEncoder().encodeToString(("{\"id\":" + id + "}").getBytes(StandardCharsets.UTF_8));
  }
}