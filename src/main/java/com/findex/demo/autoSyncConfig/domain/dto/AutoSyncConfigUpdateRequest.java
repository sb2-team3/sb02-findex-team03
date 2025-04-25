package com.findex.demo.autoSyncConfig.domain.dto;

import jakarta.validation.constraints.NotNull;

public record AutoSyncConfigUpdateRequest
        (@NotNull(message = "enabled는 필수입니다.")
         Boolean enabled
        ) {
}
