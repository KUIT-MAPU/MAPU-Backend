package com.mapu.global.common.exception.handler;

import com.mapu.global.common.exception.BaseException;
import com.mapu.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

import static com.mapu.global.common.exception.errorcode.BaseExceptionErrorCode.*;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BaseException.class, NoHandlerFoundException.class, TypeMismatchException.class})
    public BaseErrorResponse handle_BadRequest(Exception exception) {
        log.error("[BaseExceptionControllerAdvice: handle_BadRequest 호출]", exception);
        return new BaseErrorResponse(URL_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseErrorResponse handle_HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("[BaseExceptionControllerAdvice: handle_HttpRequestMethodNotSupportedException 호출]", e);
        return new BaseErrorResponse(METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseErrorResponse handle_ConstraintViolationException(ConstraintViolationException e) {
        log.error("[handle_ConstraintViolationException]", e);
        return new BaseErrorResponse(BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public BaseErrorResponse handle_IllegalArgumentException(IllegalArgumentException e) {
        log.error("[BaseExceptionControllerAdvice: handle_IllegalArgumentException 호출]", e);
        return new BaseErrorResponse(BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public BaseErrorResponse handle_IllegalStatusException(IllegalStateException e) {
        log.error("[BaseExceptionControllerAdvice: handle_IllegalStatusException 호출]", e);
        return new BaseErrorResponse(BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IOException.class)
    public BaseErrorResponse handle_IOException(IOException e) {
        log.error("[BaseExceptionControllerAdvice: handle_IOException 호출]", e);
        return new BaseErrorResponse(BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseErrorResponse handle_MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[BaseExceptionControllerAdvice: handle_MethodArgumentNotValidException 호출]", e);
        return new BaseErrorResponse(INAPPROPRIATE_DATA);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public BaseErrorResponse handle_MissingServletRequestPartException(MissingServletRequestPartException e) {
        log.error("[BaseExceptionControllerAdvice: handle_MissingServletRequestPartException 호출]", e);
        return new BaseErrorResponse(INAPPROPRIATE_DATA);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public BaseErrorResponse handle_ForbiddenException(HttpClientErrorException.Forbidden e){
        log.error("[BaseExceptionControllerAdvice: handle_ForbiddenException 호출]", e);
        return new BaseErrorResponse(INAPPROPRIATE_DATA);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public BaseErrorResponse handle_RuntimeException(Exception e) {
        log.error("[BaseExceptionControllerAdvice: handle_RuntimeException 호출]", e);
        return new BaseErrorResponse(SERVER_ERROR);
    }
}
