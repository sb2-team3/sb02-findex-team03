package com.findex.demo.autoSyncConfig.repository;

import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.global.pagination.dto.PagedResponse;
import com.findex.demo.global.pagination.dto.SortDirection;
import com.findex.demo.global.pagination.dto.SortField;

public interface AutoSyncConfigRepositoryCustom {
    PagedResponse<AutoSyncConfig> getPage(Integer indexInfoId, Boolean enabled, int idAfter, String cursor,
                                          SortField sortField, SortDirection sortDirection, int size);
}
