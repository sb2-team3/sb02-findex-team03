package com.findex.demo.syncJobs.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import com.findex.demo.syncJobs.domain.dto.SyncJobDto;
import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.mapper.SyncJobMapper;
import com.findex.demo.syncJobs.repository.SyncJobRepository;
import com.findex.demo.autoSyncConfig.repository.AutoSyncConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;




@Slf4j // ✅ 추가
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

    @Transactional
    public List<SyncJobDto> createSyncJobsAndConfigs() {
        log.info("[시작] 마켓 인덱스 동기화 작업 생성 및 설정");

        fetchAndStoreMarketIndices();

        List<IndexInfo> indexInfos = indexInfoRepository.findAll();
        log.info("[조회] IndexInfo 총 개수: {}", indexInfos.size());

        List<SyncJob> syncJobs = indexInfos.stream()
            .filter(indexInfo -> indexInfo.getSourceType() == SourceType.OPEN_API)
            .map(OpenApIIndexInfoMapper::toSyncJob)
            .collect(Collectors.toList());

        List<SyncJob> savedJobs = syncJobRepository.saveAll(syncJobs);
        log.info("[저장] SyncJob 저장 완료, 총 개수: {}", savedJobs.size());

        return savedJobs.stream()
            .map(SyncJobMapper::toSyncJobDto)
            .collect(Collectors.toList());
    }

    public void fetchAndStoreMarketIndices() {
        log.info("[시작] 외부 마켓 인덱스 API 호출 및 저장");
        Set<String> seenKeys = new HashSet<>();

        for (int page = 1; page <= TOTAL_PAGES; page++) {
            try {
                String apiUrl = baseUrl +
                    "?serviceKey=" + serviceKey +
                    "&resultType=json" +
                    "&pageNo=" + page +
                    "&numOfRows=" + numOfRows;

                URI uri = new URI(apiUrl);
                log.debug("[API 호출] URL: {}", apiUrl);

                String responseString = restTemplate.getForObject(uri, String.class);

                JsonNode itemNode = objectMapper.readTree(responseString)
                    .path("response").path("body").path("items").path("item");

                if (itemNode.isMissingNode() || itemNode.isNull()) {
                    log.warn("[경고] 페이지 {} 데이터 없음", page);
                    continue;
                }

                if (itemNode.isArray()) {
                    for (JsonNode item : itemNode) {
                        processItem(item, seenKeys);
                    }
                } else {
                    processItem(itemNode, seenKeys);
                }

                log.info("[성공] 페이지 {} 처리 완료", page);

            } catch (Exception e) {
                log.error("[실패] 페이지 {} 처리 중 오류 발생: {}", page, e.getMessage(), e);
                throw new CustomException(ErrorCode.PATH_NOT_FOUND, "API 호출 또는 파싱 중 오류 발생: page " + page);
            }
        }
    }

    private void processItem(JsonNode item, Set<String> seenKeys) {
        String indexClassification = item.path("idxCsf").asText();
        String indexName = item.path("idxNm").asText();
        String key = indexClassification + "|" + indexName;

        if (!seenKeys.add(key)) {
            log.debug("[중복 스킵] {}", key);
            return;
        }

        int employedItemCount = item.path("epyItmsCnt").asInt();
        if (employedItemCount <= 0) {
            log.debug("[스킵] 종목 수 0 이하: {}", key);
            return;
        }

        try {
            ExternalIndexInfoDto dto = ExternalIndexInfoDto.builder()
                .indexClassification(indexClassification)
                .indexName(indexName)
                .employedItemCount(employedItemCount)
                .basePointInTimeRaw(item.path("basPntm").asText())
                .basId(item.path("basIdx").asInt())
                .build();

            Optional<IndexInfo> existingOpt = indexInfoRepository
                .findByIndexClassificationAndIndexName(dto.indexClassification(), dto.indexName());

            if (existingOpt.isPresent()) {
                IndexInfo indexInfo = existingOpt.get();

                if (indexInfo.getSourceType() != SourceType.OPEN_API) {
                    log.debug("[스킵] 기존 SourceType이 OPEN_API 아님: {}", key);
                    return;
                }

                indexInfo.updateFromDto(dto);
                indexInfoRepository.save(indexInfo);
                log.info("[업데이트] IndexInfo 갱신: {}", key);

                if (!autoSyncConfigRepository.existsByIndexInfoId(indexInfo.getId())) {
                    AutoSyncConfig autoSyncConfig = OpenApIIndexInfoMapper.toAutoSyncConfig(indexInfo, false);
                    autoSyncConfigRepository.save(autoSyncConfig);
                    log.info("[생성] AutoSyncConfig 추가: {}", key);
                }

            } else {
                IndexInfo indexInfo = OpenApIIndexInfoMapper.toIndexInfo(dto);

                if (indexInfo.getSourceType() != SourceType.OPEN_API) {
                    log.debug("[스킵] 새 IndexInfo SourceType이 OPEN_API 아님: {}", key);
                    return;
                }

                indexInfoRepository.save(indexInfo);
                log.info("[생성] 새 IndexInfo 저장: {}", key);

                AutoSyncConfig autoSyncConfig = OpenApIIndexInfoMapper.toAutoSyncConfig(indexInfo, false);
                autoSyncConfigRepository.save(autoSyncConfig);
                log.info("[생성] AutoSyncConfig 추가: {}", key);
            }
        } catch (Exception e) {
            log.error("[실패] 데이터 저장 중 예외 발생: {} / 예외: {}", key, e.getMessage(), e);
        }
    }
}
