package com.mapu.infra.s3.exception;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AwsS3ErrorCode implements ResponseStatus {

    /**
     * 6000: S3 오류
     */

    AWS_S3_IMAGE_UPLOAD_FAIL(6000,HttpStatus.FAILED_DEPENDENCY.value(),"AWS S3에 이미지 업로드 중 오류가 발생했습니다."),
    AWS_S3_INVALID_FILE_TYPE(6001,HttpStatus.BAD_REQUEST.value(), "이미지 파일 형식이 올바르지 않습니다."),
    AWS_S3_FILE_TOO_LARGE(6002,HttpStatus.BAD_REQUEST.value(), "이미지 업로드 크기는 최대 10MB 입니다."),
    AWS_S3_IMAGE_DELETE_FAIL(6003, HttpStatus.BAD_REQUEST.value(), "AWS S3에 존재하는 이미지 삭제 중 오류가 발생했습니다."),
    AWS_S3_INVALID_FILE_URL(6004, HttpStatus.BAD_REQUEST.value(), "이미지 URL 형식이 올바르지 않습니다.");


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
