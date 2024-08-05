package com.mapu.domain.map.exception.handler;

import com.mapu.domain.map.exception.SearchException;
import com.mapu.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SearchExceptionHandler {
    @ExceptionHandler(SearchException.class)
    public BaseErrorResponse handleSearchException(SearchException exception) {
        log.error("[SearchException: handle_SearchException 호출]", exception);
        return new BaseErrorResponse(exception.getResponseStatus(), exception.getMessage());
    }
}
