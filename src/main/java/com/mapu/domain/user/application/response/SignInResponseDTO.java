package com.mapu.domain.user.application.response;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SignInResponseDTO {
    private String message;

    public SignInResponseDTO(String message) {
        this.message = message;
    }
}
