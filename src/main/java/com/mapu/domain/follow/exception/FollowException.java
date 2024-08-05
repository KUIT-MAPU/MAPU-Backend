package com.mapu.domain.follow.exception;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class FollowException extends RuntimeException{
    private final ResponseStatus exceptionStatus;

    public FollowException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

}
