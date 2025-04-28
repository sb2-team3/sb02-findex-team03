package com.findex.demo.autoSyncConfig.service;

import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigDto;
import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigUpdateRequest;
import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.autoSyncConfig.mapper.AutoSyncConfigMapper;
import com.findex.demo.autoSyncConfig.repository.AutoSyncConfigRepository;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.global.pagination.dto.PagedResponse;
import com.findex.demo.global.pagination.dto.SortDirection;
import com.findex.demo.global.pagination.dto.SortField;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AutoSyncConfigServiceImpl implements AutoSyncConfigService{

    private final AutoSyncConfigRepository autoSyncConfigRepository;
    private final IndexInfoRepository indexInfoRepository;

    @Override
    @Transactional
    public AutoSyncConfigDto updateAutoSyncConfig(Integer indexInfoId, AutoSyncConfigUpdateRequest request) {
        IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "자동 연동된 지수를 찾을 수 없습니다"));
        AutoSyncConfig autoSyncConfig;
        Optional<AutoSyncConfig> existingConfig = autoSyncConfigRepository.findByIndexInfo_Id(indexInfoId);

        if (existingConfig.isPresent()) {
            autoSyncConfig = existingConfig.get();
            autoSyncConfig.update(request.enabled());
        } else {
            autoSyncConfig = AutoSyncConfigMapper.toAutoSyncConfig(request.enabled(), indexInfo);
        }

        autoSyncConfigRepository.save(autoSyncConfig);
        return AutoSyncConfigMapper.toAutoSyncConfigDto(autoSyncConfig);
    }



    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AutoSyncConfigDto> getPageAutoSynConfig(Integer indexInfoId, Boolean enabled, int idAfter, String cursor,
                                                                 SortField sortField, SortDirection sortDirection, int size) {

        PagedResponse<AutoSyncConfig> entityPage = autoSyncConfigRepository.getPage(indexInfoId, enabled, idAfter, cursor, sortField, sortDirection, size);
        List<AutoSyncConfigDto> dtoList = entityPage.content().stream()
                .map(AutoSyncConfigMapper::toAutoSyncConfigDto)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                dtoList,
                entityPage.nextCursor(),
                entityPage.nextIdAfter(),
                entityPage.size(),
                dtoList.size(),
                entityPage.hasNext()
        );

    }

}
