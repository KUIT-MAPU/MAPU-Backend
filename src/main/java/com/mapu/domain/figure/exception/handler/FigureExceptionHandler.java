package com.mapu.domain.figure.exception.handler;

import com.mapu.domain.figure.exception.FigureException;
import com.mapu.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class FigureExceptionHandler {

    @ExceptionHandler(value = {FigureException.class})
    public BaseErrorResponse handleException(FigureException e) {
        log.error("[Figure Exception: handle_FigureException]", e);
        return new BaseErrorResponse(e.getExceptionStatus(),e.getMessage());
    }

}
