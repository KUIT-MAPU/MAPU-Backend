package com.mapu.global.common.exception.handler;

import com.mapu.global.common.exception.UserException;
import com.mapu.global.common.exception.errorcode.UserExceptionErrorCode;
import com.mapu.global.common.response.BaseErrorResponse;
import jdk.jshell.spi.ExecutionControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExecutionControl.UserException.class)
    public BaseErrorResponse handleUserException(UserException e) {
        UserExceptionErrorCode errorCode = e.getErrorCode();
        return new BaseErrorResponse(errorCode);
    }
}
