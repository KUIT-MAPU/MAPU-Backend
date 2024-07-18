package com.mapu.global.common.exception.errorcode;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserExceptionErrorCode implements ResponseStatus {

    /**
     * 4000: 사용자
     */
    LOGIN_SUCESS(4000,  HttpStatus.OK.value(), "성공적으로 로그인되었습니다."),
    LOGOUT_SUCCESS(4000, HttpStatus.OK.value(), "성공적으로 로그아웃되었습니다."),

    LOGOUT_FAIL(4001, HttpStatus.BAD_REQUEST.value(), "로그아웃이 실패했습니다."),
    NEED_TO_LOGIN(4002, HttpStatus.BAD_REQUEST.value(), "로그인이 되지 않았습니다."),

    DEREGISTRATION_SUCCESS(4003, HttpStatus.OK.value(), "성공적으로 회원탈퇴 되었습니다."),
    DEREGISTRATION_FAIL(4004, HttpStatus.BAD_REQUEST.value(), "회원탈퇴 실패했습니다."),

    /**
     * 4005: 사용자, 검증 에러
     */
    INVALID_NICKNAME(4005, HttpStatus.BAD_REQUEST.value(), "닉네임이 유효하지 않습니다."),
    INVALID_NICKNAME_LENGTH(4005, HttpStatus.BAD_REQUEST.value(), "닉네임은 3~12자 사이여야 합니다."),
    INVALID_PROFILE_ID(4005, HttpStatus.BAD_REQUEST.value(), "프로필 ID가 유효하지 않습니다."),
    INVALID_PROFILE_ID_LENGTH(4005, HttpStatus.BAD_REQUEST.value(), "프로필 ID는 3~20자 사이여야 합니다."),
    DUPLICATE_NICKNAME(4005, HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 닉네임입니다."),
    DUPLICATE_PROFILE_ID(4005, HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 프로필 ID입니다.");

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


