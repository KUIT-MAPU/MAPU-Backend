package com.mapu.global.jwt.application;

import lombok.Getter;

@Getter
public class AccessTokenResponseDto {
    private String accessToken;
    public AccessTokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}