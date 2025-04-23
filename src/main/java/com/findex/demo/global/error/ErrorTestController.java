package com.findex.demo.global.error;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorTestController {

    // 2. 커스텀 예외 발생 테스트
    @GetMapping("/test/custom")
    public String testCustom() {
        throw new CustomException(ErrorCode.VALIDATION_ERROR, "지수 데이터 가쟈져오는 데 실패 테스트용 커스텀 예외가 발생했습니다.");
    }
}
