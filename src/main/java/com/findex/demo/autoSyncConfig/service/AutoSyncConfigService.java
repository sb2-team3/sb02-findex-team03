package com.findex.demo.autoSyncConfig.service;

import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigDto;
import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigUpdateRequest;
import com.findex.demo.global.pagination.dto.PagedResponse;
import com.findex.demo.global.pagination.dto.SortDirection;
import com.findex.demo.global.pagination.dto.SortField;

public interface AutoSyncConfigService {

    AutoSyncConfigDto updateAutoSyncConfig(Integer autoSyConfigId, AutoSyncConfigUpdateRequest request);
    PagedResponse<AutoSyncConfigDto> getPageAutoSynConfig(Integer indexInfoId, Boolean enabled, int idAfter, String cursor,
                                                          SortField sortField, SortDirection sortDirection, int size);

}
