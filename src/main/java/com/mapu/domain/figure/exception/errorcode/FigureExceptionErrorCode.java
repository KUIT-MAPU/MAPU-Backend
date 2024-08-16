package com.mapu.domain.figure.exception.errorcode;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FigureExceptionErrorCode implements ResponseStatus {

    /**
     * 9000: Figure 오류
     */
    FIGURE_NOT_FOUND(9001, HttpStatus.NOT_FOUND.value(), "Figure를 찾을 수 없습니다."),
    FIGURE_TAG_NOT_FOUND(9002, HttpStatus.NOT_FOUND.value(), "Figure 태그를 찾을 수 없습니다."),
    FIGURE_RATE_NOT_FOUND(9003, HttpStatus.NOT_FOUND.value(), "Figure 평가를 찾을 수 없습니다."),
    FIGURE_OPERATION_FAILED(9004, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Figure 작업 중 오류가 발생했습니다."),
    FIGURE_RELATION_NOT_FOUND(9005, HttpStatus.NOT_FOUND.value(), "Figure 관계를 찾을 수 없습니다.");

    private final int code;
    private final int status;
    private final String message;

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
}