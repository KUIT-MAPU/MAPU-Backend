package com.mapu.domain.map.exception.handler;

import com.mapu.domain.map.exception.MapException;
import com.mapu.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MapExceptionHandler {
    @ExceptionHandler(MapException.class)
    public BaseErrorResponse handleSearchException(MapException exception) {
        log.error("[MapException: handle_SearchException 호출]", exception);
        return new BaseErrorResponse(exception.getResponseStatus(), exception.getMessage());
    }
}
