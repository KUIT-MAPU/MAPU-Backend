package com.mapu.infra.oauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuthClientConfig {

    @Value("${spring.oauth.google.client-id}")
    private String googleClientId;
    @Value("${spring.oauth.google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${spring.oauth.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.oauth.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${spring.oauth.kakao.client-secret}")
    private String kakaoClientSecret;
    @Value("${spring.oauth.kakao.admin-key}")
    private String kakaoAdminKey;

    public String getGoogleClientId() {
        return googleClientId;
    }

    public String getGoogleClientSecret() {
        return googleClientSecret;
    }

    public String getGoogleRedirectUri() {
        return googleRedirectUri;
    }

    public String getKakaoClientId() {
        return kakaoClientId;
    }

    public String getKakaoRedirectUri() {
        return kakaoRedirectUri;
    }

    public String getKakaoClientSecret() {
        return kakaoClientSecret;
    }

    public String getKakaoAdminKey() { return kakaoAdminKey; }
}

