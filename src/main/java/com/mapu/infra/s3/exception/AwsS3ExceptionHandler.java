package com.mapu.infra.s3.exception;

import com.mapu.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
public class AwsS3ExceptionHandler {
    @ExceptionHandler(AwsS3Exception.class)
    public BaseErrorResponse handle_AwsS3Exception(AwsS3Exception e) {
        log.error("[AwsS3Exception: handle_AwsS3Exception 호출]", e);
        return new BaseErrorResponse(e.getExceptionStatus(), e.getMessage());
    }
}
