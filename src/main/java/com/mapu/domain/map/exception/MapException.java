package com.mapu.domain.map.exception;

import com.mapu.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class MapException extends RuntimeException {
    private final ResponseStatus responseStatus;

    public MapException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.responseStatus = responseStatus;
    }
}
