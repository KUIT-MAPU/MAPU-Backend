package com.mapu.domain.follow.exception.handler;

import com.mapu.domain.follow.exception.FollowException;
import com.mapu.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class FollowExceptionHandler {
    @ExceptionHandler(value = {FollowException.class})
    public BaseErrorResponse handleException(FollowException e) {
        log.error("[Follow Exception: handle_UserException 호출]",e);
        return new BaseErrorResponse(e.getExceptionStatus(),e.getMessage());
    }
}
