package com.findex.demo.indexData.index.controller;

import com.findex.demo.indexData.index.domain.dto.CursorPageResponseIndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.dto.IndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataSearchCondition;
import com.findex.demo.indexData.index.domain.dto.IndexDataUpdateRequest;
import com.findex.demo.indexData.index.service.IndexDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/index-data")
public class IndexDataController {

  private final IndexDataService indexDataService;
  /**
   * 지수 데이터 목록조회
   */
  @GetMapping
  public ResponseEntity<CursorPageResponseIndexDataDto> create(@RequestBody @Valid
  IndexDataSearchCondition request) {
    CursorPageResponseIndexDataDto dto = indexDataService.findAll(request);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }
  /**

  /**
   * 지수 데이터 생성
   */
  @PostMapping
  public ResponseEntity<IndexDataDto> create(@RequestBody @Valid IndexDataCreateRequest request) {
    IndexDataDto created = indexDataService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }
  /**
   * 지수 데이터 수정
   */
  @PutMapping("/{id}")
  public ResponseEntity<IndexDataDto> update(
      @PathVariable Integer id,
      @RequestBody @Valid IndexDataUpdateRequest request) {
    IndexDataDto updated = indexDataService.update(id, request);
    return ResponseEntity.ok(updated);
  }

  /**
   * 지수 데이터 삭제
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    indexDataService.delete(id);
    return ResponseEntity.noContent().build(); // HTTP 204
  }
}


