package com.findex.demo.autoSyncConfig.repository;

import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoSyncConfigRepository extends JpaRepository<AutoSyncConfig, Integer> {
}
