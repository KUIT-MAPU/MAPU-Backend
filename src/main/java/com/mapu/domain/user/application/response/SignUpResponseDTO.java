package com.mapu.domain.user.application.response;

import lombok.Getter;

@Getter
public class SignUpResponseDTO {
    private String imageURL;

    public SignUpResponseDTO(String imageURL) {
        this.imageURL = imageURL;
    }
}
