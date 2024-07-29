package com.mapu.domain.follow.exception.errorcode;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FollowExceptionErrorCode implements ResponseStatus {
    /**
     * 7000: Follow 오류
     */
    FOLLOW_NOT_FOUND(7000, HttpStatus.NOT_FOUND.value(), "팔로우 관계를 찾을 수 없습니다."),
    SELF_FOLLOW_NOT_ALLOWED(7001, HttpStatus.BAD_REQUEST.value(), "자기 자신을 팔로우할 수 없습니다."),
    ALREADY_FOLLOWING(7002, HttpStatus.BAD_REQUEST.value(), "이미 팔로우 중인 사용자입니다."),
    NOT_FOLLOWING(7003, HttpStatus.BAD_REQUEST.value(), "팔로우 중이지 않은 사용자입니다."),
    FOLLOW_CREATE_FAIL(7004, HttpStatus.INTERNAL_SERVER_ERROR.value(), "팔로우 생성 중 오류가 발생했습니다."),
    FOLLOW_DELETE_FAIL(7005, HttpStatus.INTERNAL_SERVER_ERROR.value(), "팔로우 삭제 중 오류가 발생했습니다."),
    INVALID_FOLLOW_REQUEST(7006, HttpStatus.BAD_REQUEST.value(), "잘못된 팔로우 요청입니다."),
    FOLLOW_LIST_RETRIEVE_FAIL(7007, HttpStatus.INTERNAL_SERVER_ERROR.value(), "팔로우 목록 조회 중 오류가 발생했습니다."),
    FOLLOWING_LIST_RETRIEVE_FAIL(7008, HttpStatus.INTERNAL_SERVER_ERROR.value(), "팔로잉 목록 조회 중 오류가 발생했습니다."),

    // 홈 부분 지도 개수 + 팔로잉 팔로워 개수 조회 ?
    FOLLOW_COUNT_RETRIEVE_FAIL(7009, HttpStatus.INTERNAL_SERVER_ERROR.value(), "팔로우 수 조회 중 오류가 발생했습니다."),
    FOLLOWING_COUNT_RETRIEVE_FAIL(7010, HttpStatus.INTERNAL_SERVER_ERROR.value(), "팔로잉 수 조회 중 오류가 발생했습니다.");


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
