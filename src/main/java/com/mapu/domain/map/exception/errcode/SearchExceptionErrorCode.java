package com.mapu.domain.map.exception.errcode;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum SearchExceptionErrorCode implements ResponseStatus {

    SOCIALTYPE_ERROR(7000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 소셜타입입니다.");

    private final int code;
    private final int status;
    private final String message;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
