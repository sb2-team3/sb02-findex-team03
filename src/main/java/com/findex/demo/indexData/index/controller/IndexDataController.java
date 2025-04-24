package com.findex.demo.indexData.index.controller;

import com.findex.demo.indexData.index.domain.dto.CursorPageResponseIndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.dto.IndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataSearchCondition;
import com.findex.demo.indexData.index.domain.dto.IndexDataUpdateRequest;
import com.findex.demo.indexData.index.service.CSVExportIndexDataService;
import com.findex.demo.indexData.index.service.IndexDataService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/index-data")
public class IndexDataController {

  private final IndexDataService indexDataService;
  private final CSVExportIndexDataService csvExportIndexDataService;
  /*
  TODO : 목요일 에  같이 구현
   */
  /**
   * 지수 데이터 목록조회
   */
  @GetMapping
  public ResponseEntity<CursorPageResponseIndexDataDto> findAll(
      @RequestParam(defaultValue = "1") Integer indexInfoId,
      @RequestParam(defaultValue = "2010-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(defaultValue = "2025-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam Integer idAfter,
      @RequestParam String cursor,
      @RequestParam(defaultValue = "baseDate") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection,
      @RequestParam(defaultValue = "10") Integer size
  ) {
    IndexDataSearchCondition request = new IndexDataSearchCondition(
        indexInfoId, startDate, endDate, idAfter, cursor,
        sortField, sortDirection, size
    );
    CursorPageResponseIndexDataDto dto = indexDataService.findAll(request);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }
  /**

  /**
   * 지수 데이터 생성
   */
  @PostMapping
  public ResponseEntity<IndexDataDto> create(@RequestBody IndexDataCreateRequest request) {
    IndexDataDto created = indexDataService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }
  /**
   * 지수 데이터 수정
   */
  @PutMapping("/{indexDataId}")
  public ResponseEntity<IndexDataDto> update(
      @PathVariable Integer indexDataId,
      @RequestBody  IndexDataUpdateRequest request) {
    IndexDataDto updated = indexDataService.update(indexDataId, request);
    return ResponseEntity.ok(updated);
  }

  /**
   * 지수 데이터 삭제
   */
  @DeleteMapping("/{indexDataId}")
  public ResponseEntity<Void> delete(@PathVariable Integer indexDataId) {
    indexDataService.delete(indexDataId);
    return ResponseEntity.noContent().build(); // HTTP 204
  }

  /**
   * csv export
   */
  @GetMapping("/export/csv")
  public ResponseEntity<byte[]> downloadCsvFile() throws Exception{

    byte[] response = csvExportIndexDataService.downloadCsv();

    if(response == null) {
      return ResponseEntity.internalServerError().build();
    }

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv");
    return new ResponseEntity<>(response, headers, HttpStatus.OK);
  }
}


