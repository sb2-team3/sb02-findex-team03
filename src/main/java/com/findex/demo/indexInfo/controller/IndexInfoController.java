package com.findex.demo.indexInfo.controller;

import com.findex.demo.indexInfo.domain.dto.*;
import com.findex.demo.indexInfo.service.IndexInfoService;
import jakarta.validation.Valid;
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
      @RequestBody @Valid IndexInfoCreateRequest request) {
    return ResponseEntity.ok(indexInfoService.create(request));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<IndexInfoDto> update(
      @PathVariable Integer id,
      @RequestBody @Valid IndexInfoUpdateRequest request) {
    return ResponseEntity.ok(indexInfoService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable Integer id) {
    indexInfoService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<IndexInfoDto> get(
      @PathVariable Integer id) {
    return ResponseEntity.ok(indexInfoService.getIndexInfo(id));
  }

  @GetMapping("/summaries")
  public ResponseEntity<List<IndexInfoSummaryDto>> getSummaries() {
    return ResponseEntity.ok(indexInfoService.getIndexInfoSummaries());
  }

  @GetMapping
  public ResponseEntity<CursorPageResponseIndexInfoDto> getIndexInfoList(
      @RequestParam(required = false) String indexClassification,
      @RequestParam(required = false) String indexName,
      @RequestParam(required = false) Boolean favorite,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "asc") String sortDirection,
      @RequestParam(defaultValue = "10") int size) {

    CursorPageResponseIndexInfoDto response = indexInfoService.getIndexInfoList(
        indexClassification,
        indexName,
        favorite,
        idAfter,
        cursor,
        sortField,
        sortDirection,
        size
    );

    return ResponseEntity.ok(response);
  }

}
