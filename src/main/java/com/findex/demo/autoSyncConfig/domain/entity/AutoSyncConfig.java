package com.findex.demo.autoSyncConfig.domain.entity;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AutoSyncConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "index_info_id")
    private IndexInfo indexInfo;

    @Column(nullable = false)
    private Boolean enabled;

    public void update(Boolean enabled) {
        if (enabled != null) {
            this.enabled = enabled;
        }
    }
}
