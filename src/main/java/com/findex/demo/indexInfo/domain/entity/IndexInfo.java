package com.findex.demo.indexInfo.domain.entity;

import com.findex.demo.syncJobs.api.ExternalIndexInfoDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class IndexInfo {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String indexClassification;
  private String indexName;
  private int employedItemCount;
  private LocalDate basePointInTime;
  private int baseIndex;

  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  private boolean favorite = false;

  @Builder
  public IndexInfo(String indexClassification, String indexName, int employedItemCount, LocalDate basePointInTime,
                   Integer baseIndex, SourceType sourceType, boolean favorite) {
    this.indexClassification = indexClassification;
    this.indexName = indexName;
    this.employedItemCount = employedItemCount;
    this.basePointInTime = basePointInTime;
    this.baseIndex = baseIndex;
    this.sourceType = sourceType;
    this.favorite = favorite;
  }

  public void setBasePointInTime(LocalDate basePointInTime) {
    this.basePointInTime = basePointInTime;
  }

  public void setBaseIndex(int baseIndex) {
    this.baseIndex = baseIndex;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  public void setEmployedItemCount(int employedItemCount) {
    this.employedItemCount = employedItemCount;
  }

  public void updateFromDto(ExternalIndexInfoDto dto) {
    this.baseIndex = dto.basId();
    this.employedItemCount = dto.employedItemCount();
    this.basePointInTime = LocalDate.parse(dto.basePointInTimeRaw(), DateTimeFormatter.ofPattern("yyyyMMdd"));
  }
}
