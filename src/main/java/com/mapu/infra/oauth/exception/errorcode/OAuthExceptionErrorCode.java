package com.mapu.infra.oauth.exception.errorcode;

import com.mapu.global.common.response.status.ResponseStatus;
import jakarta.servlet.annotation.HandlesTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum OAuthExceptionErrorCode implements ResponseStatus {

    /**
     * 5000: OAUTH 오류
     */
    NEED_TO_SIGNUP(5000, HttpStatus.OK.value(), "회원가입이 필요한 유저입니다."),
    GOOGLE_LOGIN_FAIL(5001,HttpStatus.BAD_REQUEST.value(),"GOOGLE 소셜 로그인 중 오류가 발생했습니다."),
    KAKAO_LOGIN_FAIL(5002,HttpStatus.BAD_REQUEST.value(),"KAKAO 소셜 로그인 중 오류가 발생했습니다."),
    INVALID_SOCIAL_LOGIN_TYPE(5003, HttpStatus.BAD_REQUEST.value(),"유효하지 않은 소셜로그인 유형입니다."),
    GET_TOKEN_FAIL(5004, HttpStatus.BAD_REQUEST.value(), "SOCIAL TOKEN이 제대로 생성되지 않았습니다");

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


