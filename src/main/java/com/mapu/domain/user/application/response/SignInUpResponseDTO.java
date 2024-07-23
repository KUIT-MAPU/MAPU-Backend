package com.mapu.domain.user.application.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInUpResponseDTO {
    private String imgUrl;
    private String profileId;

    @Builder
    public SignInUpResponseDTO(String imgUrl, String profileId) {
        this.imgUrl = imgUrl;
        this.profileId = profileId;
    }
}
