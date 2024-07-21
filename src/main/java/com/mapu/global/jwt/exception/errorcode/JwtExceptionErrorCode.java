package com.mapu.global.jwt.exception.errorcode;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public enum JwtExceptionErrorCode implements ResponseStatus {

    /**
     * 7000: JWT 오류
     */
    ERROR_IN_JWT(7000, HttpStatus.INTERNAL_SERVER_ERROR.value(), "JWT 토큰 발급 중 오류가 발생했습니다."),
    NO_JWT_TOKEN_IN_COOKIE(7001, HttpStatus.BAD_REQUEST.value()),
    EXPIRED_JWT_TOKEN(7002, HttpStatus.BAD_REQUEST.value()),
    INVALID_JWT_TOKEN(7003,HttpStatus.BAD_REQUEST.value());


    JwtExceptionErrorCode(int code, int status) {
        this.code = code;
        this.status = status;
        this.message = "";
    }

    JwtExceptionErrorCode(int code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    private final int code;
    private final int status;
    private String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) { this.message = message; }
}
