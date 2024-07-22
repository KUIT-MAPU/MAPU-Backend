package com.mapu.global.jwt.exception;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException{
    private final ResponseStatus exceptionStatus;

    public JwtException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}