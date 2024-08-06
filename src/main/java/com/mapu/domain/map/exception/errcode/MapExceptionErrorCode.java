package com.mapu.domain.map.exception.errcode;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MapExceptionErrorCode implements ResponseStatus {

    NOT_FOUND_MAP(7001, HttpStatus.BAD_REQUEST.value(), "해당하는 map이 없습니다."),
    NOT_FOUND_BOOKMARK(7002, HttpStatus.BAD_REQUEST.value(), "북마크되어 있는 Map이 아닙니다."),
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
