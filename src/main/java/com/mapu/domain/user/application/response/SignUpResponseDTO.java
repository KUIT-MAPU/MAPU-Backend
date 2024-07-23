package com.mapu.domain.user.application.response;

import lombok.Getter;

@Getter
public class SignUpResponseDTO {
    private String imageURL;
    private String accessToken;

    public SignUpResponseDTO(String imageURL, String accessToken) {
        this.imageURL = imageURL;
        this.accessToken = accessToken;
    }
}
