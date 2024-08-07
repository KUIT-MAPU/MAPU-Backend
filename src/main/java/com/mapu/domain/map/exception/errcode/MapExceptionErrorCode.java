package com.mapu.domain.map.exception.errcode;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MapExceptionErrorCode implements ResponseStatus {

    SOCIALTYPE_ERROR(7000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 소셜타입입니다."),
    ALREADY_EDITOR(7001, HttpStatus.BAD_REQUEST.value(), "이미 편집권한을 소유한 유저입니다."),
    NO_EXIST_MAP(7002, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 지도입니다.");
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
