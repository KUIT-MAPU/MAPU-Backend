package com.mapu.infra.oauth.exception;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class OAuthException extends RuntimeException{
    private final ResponseStatus exceptionStatus;

    public OAuthException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
