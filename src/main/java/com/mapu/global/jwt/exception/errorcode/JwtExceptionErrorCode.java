package com.mapu.global.jwt.exception.errorcode;

import com.mapu.global.common.response.status.ResponseStatus;
import org.springframework.http.HttpStatus;

public enum JwtExceptionErrorCode implements ResponseStatus {

    /**
     * 7000: JWT 오류
     */
    ERROR_IN_JWT(7000, HttpStatus.INTERNAL_SERVER_ERROR.value(), "JWT 토큰 발급 중 오류가 발생했습니다."),
    NO_JWT_TOKEN(7001, HttpStatus.UNAUTHORIZED.value(), "JWT 토큰이 없습니다."),
    NO_BEARER_TYPE(7002, HttpStatus.UNAUTHORIZED.value(),"Bearer 토큰이 아닙니다."),
    EXPIRED_JWT_TOKEN(7003, HttpStatus.UNAUTHORIZED.value(), "만료된 JWT 토큰입니다."),
    WRONG_JWT_TOKEN_TYPE(7004, HttpStatus.UNAUTHORIZED.value(), "JWT 토큰 타입이 올바르지 않습니다."),
    UNKNOWN_REFRESH_TOKEN(7005, HttpStatus.BAD_REQUEST.value(), "서버에 등록되어 있지 않은 refresh JWT 토큰입니다."),
    INVALID_JWT_TOKEN(7006, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT 토큰입니다."),
    MALFORMED_JWT_TOKEN(7007,HttpStatus.BAD_REQUEST.value(), "JWT 토큰 형식이 올바르지 않습니다.");

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

    public void addTokenTypeInfoToMessage(String tokenType) { this.message = this.message + " (" + tokenType + ")"; }
}
