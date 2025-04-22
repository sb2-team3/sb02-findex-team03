package com.findex.demo.global.error;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String message;
    private final String details;

    public ErrorResponse(ErrorCode errorCode, String details) {
        this.timestamp = LocalDateTime.now();
        this.status = errorCode.getStatus().value();
        this.message = errorCode.getMessage();
        this.details = details;
    }

    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode, null);
    }

}
