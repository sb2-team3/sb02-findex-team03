package com.findex.demo.syncJobs.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import com.findex.demo.syncJobs.api.ExternalIndexInfoDto;
import com.findex.demo.syncJobs.domain.dto.OpenApiSyncResultResponse;
import com.findex.demo.syncJobs.api.OpenApIIndexInfoMapper;
import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketIndexSyncService {

    private final IndexInfoRepository indexInfoRepository;
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

    public OpenApiSyncResultResponse fetchAndStoreMarketIndices() {
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

                log.info("📤 [Page {}] 요청 URI: {}", page, apiUrl);

                URI uri = new URI(apiUrl);
                String responseString = restTemplate.getForObject(uri, String.class);

                JsonNode itemNode = objectMapper.readTree(responseString)
                    .path("response").path("body").path("items").path("item");

                if (itemNode.isMissingNode() || itemNode.isNull()) {
                    log.warn("⚠️ [Page {}] 'item' 노드 없음, 건너뜀", page);
                    continue;
                }

                if (itemNode.isArray()) {
                    log.info("✅ [Page {}] 'item' 배열 총 {}건", page, itemNode.size());
                    for (JsonNode item : itemNode) {
                        int result = processItem(item, seenKeys);
                        if (result == 1) createdCount++;
                        else if (result == 2) updatedCount++;
                        else skippedCount++;
                    }
                } else {
                    log.info("✅ [Page {}] 단일 'item' 처리", page);
                    int result = processItem(itemNode, seenKeys);
                    if (result == 1) createdCount++;
                    else if (result == 2) updatedCount++;
                    else skippedCount++;
                }

            } catch (Exception e) {
                log.error("❌ 예외 발생: {}", e.getMessage(), e);
                throw new CustomException(ErrorCode.PATH_NOT_FOUND, "API 호출 또는 파싱 중 오류 발생: page " + page);
            }
        }

        log.info("🏁 전체 연동 완료: 신규 {}, 수정 {}, 변동없음 {}", createdCount, updatedCount, skippedCount);

        return OpenApiSyncResultResponse.builder()
            .createdCount(createdCount)
            .updatedCount(updatedCount)
            .skippedCount(skippedCount)
            .totalCount(createdCount + updatedCount + skippedCount)
            .build();
    }

    /**
     * @return 1 = 신규 저장 / 2 = 업데이트 / 0 = 건너뜀
     */
    private int processItem(JsonNode item, Set<String> seenKeys) {
        String indexClassification = item.path("idxCsf").asText();
        String indexName = item.path("idxNm").asText();
        String key = indexClassification + "|" + indexName;

        if (!seenKeys.add(key)) {
            log.debug("🔁 중복 지수 건너뜀: {}", key);
            return 0;
        }

        ExternalIndexInfoDto dto = ExternalIndexInfoDto.builder()
            .indexClassification(indexClassification)
            .indexName(indexName)
            .employedItemCount(item.path("epyItmsCnt").asInt())
            .basePointInTimeRaw(item.path("basPntm").asText())
            .basId(item.path("basIdx").asInt())
            .build();

        try {
            Optional<IndexInfo> existing = indexInfoRepository
                .findByIndexClassificationAndIndexName(dto.indexClassification(), dto.indexName());

            if (existing.isPresent()) {
                IndexInfo indexInfo = existing.get();
                indexInfo.updateFromDto(dto);
                indexInfoRepository.save(indexInfo);
                log.info("🔁 수정 완료: {}", indexInfo.getIndexName());
                return 2;
            } else {
                IndexInfo indexInfo = OpenApIIndexInfoMapper.toIndexInfo(dto);
                indexInfoRepository.save(indexInfo);
                log.info("✅ 신규 저장 완료: {}", indexInfo.getIndexName());
                return 1;
            }

        } catch (Exception e) {
            log.error("❌ 저장 중 예외 발생: {}", e.getMessage(), e);
            return 0;
        }
    }
}
