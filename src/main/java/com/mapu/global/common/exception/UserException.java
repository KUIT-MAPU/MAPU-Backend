package com.mapu.global.common.exception;


import com.mapu.global.common.exception.errorcode.UserExceptionErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final UserExceptionErrorCode errorCode;

    public UserException(UserExceptionErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
