package com.findex.demo.indexInfo.controller;

import com.findex.demo.indexInfo.domain.dto.*;
import com.findex.demo.indexInfo.service.IndexInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/index-infos")
@RequiredArgsConstructor
public class IndexInfoController {

  private final IndexInfoService indexInfoService;

  @PostMapping
  public ResponseEntity<IndexInfoDto> create(
      @RequestBody IndexInfoCreateRequest request) {
    return ResponseEntity.ok(indexInfoService.createIndexInfo(request));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<IndexInfoDto> update(
      @PathVariable Long id,
      @RequestBody IndexInfoUpdateRequest request) {
    return ResponseEntity.ok(indexInfoService.updateIndexInfo(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable Long id) {
    indexInfoService.deleteIndexInfo(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<IndexInfoDto> get(
      @PathVariable Long id) {
    return ResponseEntity.ok(indexInfoService.getIndexInfo(id));
  }

  @GetMapping("/summaries")
  public ResponseEntity<List<IndexInfoSummaryDto>> getSummaries() {
    return ResponseEntity.ok(indexInfoService.getIndexInfoSummaries());
  }

}
