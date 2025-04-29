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
        fetchAndStoreMarketIndices();

        List<IndexInfo> indexInfos = indexInfoRepository.findAll();

        List<SyncJob> syncJobs = indexInfos.stream()
                .filter(indexInfo -> indexInfo.getSourceType() == SourceType.OPEN_API)
                .map(OpenApIIndexInfoMapper::toSyncJob)
                .collect(Collectors.toList());

        List<SyncJob> savedJobs = syncJobRepository.saveAll(syncJobs);

        return savedJobs.stream()
                .map(SyncJobMapper::toSyncJobDto)
                .collect(Collectors.toList());
    }

    public void fetchAndStoreMarketIndices() {
        Set<String> seenKeys = new HashSet<>();

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
                        processItem(item, seenKeys); // ✅ void 메서드로 수정
                    }
                } else {
                    processItem(itemNode, seenKeys);
                }

            } catch (Exception e) {
                throw new CustomException(ErrorCode.PATH_NOT_FOUND, "API 호출 또는 파싱 중 오류 발생: page " + page);
            }
        }
    }

    private void processItem(JsonNode item, Set<String> seenKeys) {
        String indexClassification = item.path("idxCsf").asText();
        String indexName = item.path("idxNm").asText();
        String key = indexClassification + "|" + indexName;

        if (!seenKeys.add(key)) {
            return; // 이미 처리한 키는 스킵
        }

        int employedItemCount = item.path("epyItmsCnt").asInt();
        if (employedItemCount <= 0) {
            return; // 종목 수 0 이하 스킵
        }

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
                return; // 기존 데이터가 OPEN_API 아니면 무시
            }

            indexInfo.updateFromDto(dto);
            indexInfoRepository.save(indexInfo);

            if (!autoSyncConfigRepository.existsByIndexInfoId(indexInfo.getId())) {
                AutoSyncConfig autoSyncConfig = OpenApIIndexInfoMapper.toAutoSyncConfig(indexInfo, false);
                autoSyncConfigRepository.save(autoSyncConfig);
            }

        } else {
            IndexInfo indexInfo = OpenApIIndexInfoMapper.toIndexInfo(dto);

            if (indexInfo.getSourceType() != SourceType.OPEN_API) {
                return;
            }

            indexInfoRepository.save(indexInfo);

            AutoSyncConfig autoSyncConfig = OpenApIIndexInfoMapper.toAutoSyncConfig(indexInfo, false);
            autoSyncConfigRepository.save(autoSyncConfig);
        }
    }
}
