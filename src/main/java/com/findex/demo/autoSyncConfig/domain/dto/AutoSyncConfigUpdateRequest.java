package com.findex.demo.autoSyncConfig.domain.dto;

import jakarta.validation.constraints.NotNull;

public record AutoSyncConfigUpdateRequest
        (@NotNull
         Boolean enabled
        ) {
}
