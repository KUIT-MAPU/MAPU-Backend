package com.mapu.infra.s3.exception;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class AwsS3Exception extends RuntimeException{
    private final ResponseStatus exceptionStatus;

    public AwsS3Exception(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public AwsS3Exception(String exceptionMessage, ResponseStatus exceptionStatus) {
        super(exceptionMessage);
        this.exceptionStatus = exceptionStatus;
    }
}
