package com.mapu.infra.oauth.exception.handler;

import com.mapu.domain.user.exception.UserException;
import com.mapu.domain.user.exception.errorcode.UserExceptionErrorCode;
import com.mapu.global.common.response.BaseErrorResponse;
import com.mapu.infra.oauth.exception.OAuthException;
import com.mapu.infra.s3.exception.AwsS3Exception;
import jdk.jshell.spi.ExecutionControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class OAuthExceptionHandler {
    @ExceptionHandler(OAuthException.class)
    public BaseErrorResponse handle_OAuthException(OAuthException e) {
        log.error("[OAuthException: handle_OAuthException 호출]", e);
        return new BaseErrorResponse(e.getExceptionStatus(), e.getMessage());
    }
}
