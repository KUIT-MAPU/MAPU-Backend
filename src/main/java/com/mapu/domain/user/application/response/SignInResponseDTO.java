package com.mapu.domain.user.application.response;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SignInResponseDTO {
    private String message;
    private String accessToken;

    public SignInResponseDTO(String message, String accessToken) {
        this.message = message;
        this.accessToken = accessToken;
    }
}
