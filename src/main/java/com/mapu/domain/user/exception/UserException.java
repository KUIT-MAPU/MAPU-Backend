package com.mapu.domain.user.exception;


import com.mapu.domain.user.exception.errorcode.UserExceptionErrorCode;
import com.mapu.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public UserException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
