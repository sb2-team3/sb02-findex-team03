package com.findex.demo.indexInfo.domain.entity;

import com.findex.demo.indexInfo.domain.dto.IndexInfoUpdateRequest;
import com.findex.demo.syncJobs.api.ExternalIndexInfoDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "index_info")
@NoArgsConstructor
public class IndexInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String indexClassification;

  @Column(name = "index_name")
  private String indexName;
  private int employedItemCount;
  private LocalDate basePointInTime;
  private int baseIndex;

  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  private Boolean favorite = false;

  @Builder
  public IndexInfo(String indexClassification, String indexName, int employedItemCount, LocalDate basePointInTime,
                   Integer baseIndex, SourceType sourceType, Boolean favorite) {
    this.indexClassification = indexClassification;
    this.indexName = indexName;
    this.employedItemCount = employedItemCount;
    this.basePointInTime = basePointInTime;
    this.baseIndex = baseIndex;
    this.sourceType = sourceType;
    this.favorite = (favorite != null) ? favorite : false;
  }

  public void update(IndexInfoUpdateRequest updateRequest) {
    if (updateRequest.employedItemsCount() != 0 && updateRequest.employedItemsCount() != this.employedItemCount) {
      this.employedItemCount = updateRequest.employedItemsCount();
    }

    if (updateRequest.basePointInTime() != null && !updateRequest.basePointInTime().equals(this.basePointInTime)) {
      this.basePointInTime = updateRequest.basePointInTime();
    }

    if (updateRequest.baseIndex() != 0 && updateRequest.baseIndex() != this.baseIndex) {
      this.baseIndex = updateRequest.baseIndex();
    }
    if (updateRequest.favorite() != null && !updateRequest.favorite().equals(this.favorite)) {
      this.favorite = updateRequest.favorite();
    } else if (updateRequest.favorite() == null && this.favorite == null) {
      this.favorite = false;
    }
  }

  public void updateFromDto(ExternalIndexInfoDto dto) {
    this.baseIndex = dto.basId();
    this.employedItemCount = dto.employedItemCount();
    this.basePointInTime = LocalDate.parse(dto.basePointInTimeRaw(), DateTimeFormatter.ofPattern("yyyyMMdd"));
  }
}
