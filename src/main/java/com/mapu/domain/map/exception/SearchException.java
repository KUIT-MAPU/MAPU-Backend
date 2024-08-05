package com.mapu.domain.map.exception;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class SearchException extends RuntimeException {
    private final ResponseStatus responseStatus;

    public SearchException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.responseStatus = responseStatus;
    }
}
