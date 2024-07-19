package com.mapu.infra.oauth.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthUserInfo {
    String socialId;
    String email;
    String socialProvider;

    @Builder
    public OAuthUserInfo(String socialId, String email, String socialProvider) {
        this.socialId = socialId;
        this.email = email;
        this.socialProvider = socialProvider;
    }
}
