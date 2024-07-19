package com.mapu.domain.user.exception.errorcode;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserExceptionErrorCode implements ResponseStatus {

    /**
     * 4000: User 오류
     */
    INVALID_NICKNAME(4000, HttpStatus.BAD_REQUEST.value(), "닉네임이 유효하지 않습니다."),
    INVALID_NICKNAME_LENGTH(4001, HttpStatus.BAD_REQUEST.value(), "닉네임은 3~12자 사이여야 합니다."),
    INVALID_PROFILE_ID(4002, HttpStatus.BAD_REQUEST.value(), "프로필 ID가 유효하지 않습니다."),
    INVALID_PROFILE_ID_LENGTH(4003, HttpStatus.BAD_REQUEST.value(), "프로필 ID는 3~20자 사이여야 합니다."),
    DUPLICATE_NICKNAME(4004, HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 닉네임입니다."),
    DUPLICATE_PROFILE_ID(4005, HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 프로필 ID입니다."),

    NO_SESSION(4006, HttpStatus.BAD_REQUEST.value(), "세션이 없습니다."),
    NO_INFO_IN_SESSION(4007, HttpStatus.BAD_REQUEST.value(), "세션에 저장된 정보가 없습니다."),

    REMOVE_SESSION_DATA_FAIL(4008, HttpStatus.INTERNAL_SERVER_ERROR.value(), "세션 정보 삭제 중 오류가 발생했습니다."),
    LOGOUT_FAIL(4009, HttpStatus.BAD_REQUEST.value(), "로그아웃이 실패했습니다."),
    DEREGISTRATION_FAIL(4010, HttpStatus.BAD_REQUEST.value(), "회원탈퇴 실패했습니다."),

    ALREADY_SIGNUP(4011, HttpStatus.BAD_REQUEST.value(), "이미 회원가입된 사용자입니다.");

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

