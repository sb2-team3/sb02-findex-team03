package com.findex.demo.indexInfo.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@NoArgsConstructor
public class IndexInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String indexClassification;
  private String indexName;
  private int employedItemCount;
  private LocalDateTime basePointInTime;
  private double baseIndex;

  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  private boolean favorite = false;
}
