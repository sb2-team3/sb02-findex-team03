package com.findex.demo.autoSyncConfig.service;

import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigDto;
import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigUpdateRequest;
import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.autoSyncConfig.mapper.AutoSyncConfigMapper;
import com.findex.demo.autoSyncConfig.repository.AutoSyncConfigRepository;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class AutoSyncConfigServiceImpl implements AutoSyncConfigService{

    private final AutoSyncConfigRepository autoSyncConfigRepository;
    private final AutoSyncConfigMapper autoSyncConfigMapper;

    public AutoSyncConfigServiceImpl(AutoSyncConfigRepository autoSyncConfigRepository,
                                     AutoSyncConfigMapper autoSyncConfigMapper) {
        this.autoSyncConfigRepository = autoSyncConfigRepository;
        this.autoSyncConfigMapper = autoSyncConfigMapper;
    }
    // private final IndexInfoRepository indexInfoRepository;


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
}
