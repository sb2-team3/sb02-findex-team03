package com.findex.demo.syncJobs.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import com.findex.demo.syncJobs.domain.dto.SyncJobDto;
import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.mapper.SyncJobMapper;
import com.findex.demo.syncJobs.repository.SyncJobRepository;
import com.findex.demo.autoSyncConfig.repository.AutoSyncConfigRepository;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class MarketIndexSyncService {

    private final IndexInfoRepository indexInfoRepository;
    private final SyncJobRepository syncJobRepository;
    private final AutoSyncConfigRepository autoSyncConfigRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${external.market-index.service-key}")
    private String serviceKey;

    @Value("${external.market-index.base-url}")
    private String baseUrl;

    @Value("${external.market-index.num-of-rows:100}")
    private int numOfRows;

    private static final int TOTAL_COUNT = 100;
    private static final int TOTAL_PAGES = (int) Math.ceil((double) TOTAL_COUNT / 100);

    /**
     * 1. 외부 API로부터 시장 지수 데이터 수집 및 저장
     * 2. 수집된 데이터 기반으로 SyncJob 및 AutoSyncConfig 생성 및 저장
     */

    @Transactional
    public List<SyncJobDto> createSyncJobsAndConfigs() {
        // Step 1: 시장 지수 fetch + 저장
        fetchAndStoreMarketIndices();

        // Step 2: IndexInfo 전체 조회
        List<IndexInfo> indexInfos = indexInfoRepository.findAll();

        // Step 3: SyncJob 생성 및 저장
        List<SyncJob> syncJobs = indexInfos.stream()
                .map(OpenApIIndexInfoMapper::toSyncJob)
                .collect(Collectors.toList());
        List<SyncJob> savedJobs = syncJobRepository.saveAll(syncJobs);

        // Step 4: AutoSyncConfig 생성 및 저장
        List<AutoSyncConfig> autoSyncConfigs = indexInfos.stream()
                .filter(indexInfo -> !autoSyncConfigRepository.existsByIndexInfoId(indexInfo.getId()))
                .map(OpenApIIndexInfoMapper::toAutoSyncConfig)
                .collect(Collectors.toList());
        if (!autoSyncConfigs.isEmpty()) {
            autoSyncConfigRepository.saveAll(autoSyncConfigs);
        }

        // Step 5: 저장된 SyncJob을 DTO로 변환하여 반환
        return savedJobs.stream()
                .map(SyncJobMapper::toSyncJobDto)
                .collect(Collectors.toList());
    }

    /**
     * 시장 지수 데이터 Fetch 및 IndexInfo 저장
     */
    public void fetchAndStoreMarketIndices() {
        Set<String> seenKeys = new HashSet<>();
        int createdCount = 0;
        int updatedCount = 0;
        int skippedCount = 0;

        for (int page = 1; page <= TOTAL_PAGES; page++) {
            try {
                String apiUrl = baseUrl +
                        "?serviceKey=" + serviceKey +
                        "&resultType=json" +
                        "&pageNo=" + page +
                        "&numOfRows=" + numOfRows;

                URI uri = new URI(apiUrl);
                String responseString = restTemplate.getForObject(uri, String.class);

                JsonNode itemNode = objectMapper.readTree(responseString)
                        .path("response").path("body").path("items").path("item");

                if (itemNode.isMissingNode() || itemNode.isNull()) {
                    continue;
                }

                if (itemNode.isArray()) {
                    for (JsonNode item : itemNode) {
                        int result = processItem(item, seenKeys);
                        if (result == 1) {
                            createdCount++;
                        } else if (result == 2) {
                            updatedCount++;
                        } else {
                            skippedCount++;
                        }
                    }
                } else {
                    int result = processItem(itemNode, seenKeys);
                    if (result == 1) {
                        createdCount++;
                    } else if (result == 2) {
                        updatedCount++;
                    } else {
                        skippedCount++;
                    }
                }

            } catch (Exception e) {
                throw new CustomException(ErrorCode.PATH_NOT_FOUND, "API 호출 또는 파싱 중 오류 발생: page " + page);
            }
        }

    }

    /**
     * 단일 시장 지수 아이템을 저장 또는 업데이트
     *
     * @return 1 = 신규 저장 / 2 = 업데이트 / 0 = 건너뜀
     */
    private int processItem(JsonNode item, Set<String> seenKeys) {
        // 1. 기본 키 생성 (중복 방지용)
        String indexClassification = item.path("idxCsf").asText();
        String indexName = item.path("idxNm").asText();
        String key = indexClassification + "|" + indexName;

        // 2. 이미 처리한 키는 건너뜀
        if (!seenKeys.add(key)) {
            return 0;
        }

        // 3. employedItemCount 검증
        int employedItemCount = item.path("epyItmsCnt").asInt();
        if (employedItemCount <= 0) {
            return 0;
        }

        // 4. DTO 생성
        ExternalIndexInfoDto dto = ExternalIndexInfoDto.builder()
                .indexClassification(indexClassification)
                .indexName(indexName)
                .employedItemCount(employedItemCount)
                .basePointInTimeRaw(item.path("basPntm").asText())
                .basId(item.path("basIdx").asInt())
                .build();

        // 5. 기존 데이터 있는지 확인 후 저장 또는 업데이트
        Optional<IndexInfo> existingOpt = indexInfoRepository
                .findByIndexClassificationAndIndexName(dto.indexClassification(), dto.indexName());

        if (existingOpt.isPresent()) {
            IndexInfo indexInfo = existingOpt.get();
            indexInfo.updateFromDto(dto);
            indexInfoRepository.save(indexInfo);
            return 2; // 수정
        } else {
            IndexInfo indexInfo = OpenApIIndexInfoMapper.toIndexInfo(dto);
            indexInfoRepository.save(indexInfo);
            return 1; // 신규
        }
    }
}
