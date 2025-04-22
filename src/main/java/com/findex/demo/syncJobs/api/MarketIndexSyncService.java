package com.findex.demo.syncJobs.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
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

    private static final int TOTAL_COUNT = 199169;
    private static final int TOTAL_PAGES = (int) Math.ceil((double) TOTAL_COUNT / 100);

    public void fetchAndStoreMarketIndices() {
        Set<String> seenKeys = new HashSet<>();

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
                        processItem(item, seenKeys);
                    }
                } else {
                    log.info("✅ [Page {}] 단일 'item' 처리", page);
                    processItem(itemNode, seenKeys);
                }

            } catch (Exception e) {
                log.error("❌ 예외 발생: {}", e.getMessage(), e);
                throw new CustomException(ErrorCode.PATH_NOT_FOUND, "API 호출 또는 파싱 중 오류 발생: page ");
            }
        }

        log.info("🏁 전체 페이지 수집 및 저장 완료");
    }

    private void processItem(JsonNode item, Set<String> seenKeys) {
        String indexClassification = item.path("idxCsf").asText();
        String indexName = item.path("idxNm").asText();
        String key = indexClassification + "|" + indexName;

        if (!seenKeys.add(key)) {
            log.debug("🔁 중복 지수 건너뜀: {}", key);
            return;
        }

        ExternalIndexInfoDto dto = ExternalIndexInfoDto.builder()
                .indexClassification(indexClassification)
                .indexName(indexName)
                .employedItemCount(item.path("epyItmsCnt").asInt())
                .basePointInTimeRaw(item.path("basPntm").asText())
                .basId(item.path("basIdx").asInt())
                .build();

        IndexInfo indexInfo = OpenApIIndexInfoMapper.toIndexInfo(dto);
        log.info("indexInfo = {}", indexInfo.getIndexName());
        try {
            indexInfoRepository.save(indexInfo);
            log.info("✅ 저장 완료: {}", indexInfo.getIndexName());
        } catch (DataIntegrityViolationException e) {
            log.warn("⚠️ 중복된 지수 정보 무시: {}", key);
        }
    }
}
