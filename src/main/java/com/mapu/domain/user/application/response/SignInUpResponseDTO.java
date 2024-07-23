package com.mapu.domain.user.application.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInUpResponseDTO {
    private String imgUrl;
    private String profileId;
    private String accessToken;

    @Builder
    public SignInUpResponseDTO(String imgUrl, String profileId, String accessToken) {
        this.imgUrl = imgUrl;
        this.profileId = profileId;
        this.accessToken = accessToken;
    }
}
