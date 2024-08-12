package com.mapu.domain.figure.exception;


import com.mapu.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class FigureException extends RuntimeException{
    private final ResponseStatus exceptionStatus;


    public FigureException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
