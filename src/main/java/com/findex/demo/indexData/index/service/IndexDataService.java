package com.findex.demo.indexData.index.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexDataService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexDataRepository indexDataRepository;

  @Transactional(readOnly = true)
  public CursorPageResponseIndexDataDto<IndexDataDto> findAll(IndexDataSearchCondition condition) {

    Integer cursorId = decodeCursor(condition.getCursor());
    int pageSize = condition.getSize() > 0 ? condition.getSize() : 10;

    List<IndexData> results = indexDataRepository.findWithCursor(
        condition.getIndexInfoId(),
        condition.getStartDate(),
        condition.getEndDate(),
        cursorId,
        pageSize
    );

    boolean hasNext = results.size() > pageSize;
    List<IndexData> pagedResults = hasNext ? results.subList(0, pageSize) : results;

    List<IndexDataDto> content = pagedResults.stream()
        .map(data ->
            new IndexDataMapper().toIndexDto(data))
        .toList();

    String nextCursor = null;
    Integer nextIdAfter = null;

    if (hasNext && !pagedResults.isEmpty()) {
      Integer lastId = pagedResults.get(pagedResults.size() - 1).getId();
      nextCursor = encodeCursor(lastId);
      nextIdAfter = lastId;
    }

    return CursorPageResponseIndexDataDto.<IndexDataDto>builder()
        .content(content)
        .nextCursor(nextCursor)
        .nextIdAfter(nextIdAfter)
        .size(pageSize)
        .totalElements(content.size())
        .hasNext(hasNext)
        .build();
  }




  @Transactional
  public IndexDataDto create(IndexDataCreateRequest request) {

    // 1. 해당 지수 정보 존재 여부 확인
   IndexInfo indexInfo = indexInfoRepository.findById(request.getIndexInfoId())
       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE,"부서 코드는 필수 입니다."));

    // 2. 중복 확인
    boolean exists = indexDataRepository.existsByIndexInfoAndBaseDate(indexInfo, request.getBaseDate());
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
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수 입니다"));
    indexData.update(request);
    IndexData updated = indexDataRepository.save(indexData);
    return IndexDataMapper.toIndexDto(updated);
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


  public String encodeCursor(Integer id) {
    return Base64.getEncoder().encodeToString(("{\"id\":" + id + "}").getBytes(StandardCharsets.UTF_8));
  }

  public Integer decodeCursor(String cursor) {
    try {
      String json = new String(Base64.getDecoder().decode(cursor), StandardCharsets.UTF_8);
      JsonNode node = new ObjectMapper().readTree(json);
      if (!node.has("id") || node.get("id").isNull()) {
        return null;
      }
      return node.get("id").asInt();
    } catch (Exception e) {
      return null;
    }
  }


}