package com.mapu.global.jwt.exception.handler;

import com.mapu.global.common.response.BaseErrorResponse;
import com.mapu.global.jwt.exception.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class JwtExceptionHandler {
    @ExceptionHandler(JwtException.class)
    public BaseErrorResponse handle_JwtException(JwtException e) {
        log.error("[JwtException: handle_JwtException 호출]", e);
        return new BaseErrorResponse(e.getExceptionStatus(), e.getMessage());
    }
}