package com.findex.demo.global.error;

import java.time.Instant;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final Instant timestamp;
    private final int status;
    private final String message;
    private final String details;

    public ErrorResponse(ErrorCode errorCode, String details) {
        this.timestamp = Instant.now();
        this.status = errorCode.getStatus().value();
        this.message = errorCode.getMessage();
        this.details = details;
    }

    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode, null);
    }

}
