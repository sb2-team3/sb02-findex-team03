package com.findex.demo.autoSyncConfig.service;

import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigDto;
import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigUpdateRequest;
import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.autoSyncConfig.repository.AutoSyncConfigRepository;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.global.pagination.dto.PagedResponse;
import com.findex.demo.global.pagination.dto.SortDirection;
import com.findex.demo.global.pagination.dto.SortField;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AutoSyncConfigServiceImpl implements AutoSyncConfigService{

    private final AutoSyncConfigRepository autoSyncConfigRepository;
    private final IndexInfoRepository indexInfoRepository;


    @Override
    @Transactional()
    public AutoSyncConfigDto updateAutoSyncConfig(Integer autoSyConfigId, AutoSyncConfigUpdateRequest request) {
        AutoSyncConfig autoSyncConfig = autoSyncConfigRepository.findById(autoSyConfigId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "자동 연동된 지수를 찾을 수 없습니다"));
        autoSyncConfig.update(request.enabled());
        autoSyncConfigRepository.save(autoSyncConfig);
        // TODO indexInfoRepository 에서  autoSyConfigId 조회 후 정보 가져오기
        /*
        indexInfoRepository.findByAutoSyConfigId(autoSycConfigId).orElseThrow
         autoSyncConfigMapper.toAutoSyncConfigUpdateRequest(autoSyConfigId)
         */

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AutoSyncConfig> getPageAutoSynConfig(Integer indexInfoId, Boolean enabled, int idAfter, String cursor,
                                                 SortField sortField, SortDirection sortDirection, int size) {
        return autoSyncConfigRepository.getPage(indexInfoId, enabled, idAfter, cursor, sortField, sortDirection, size);
    }

}
