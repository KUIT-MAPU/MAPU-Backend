package com.mapu.global.oauth.kakao;

import lombok.Data;

@Data
public class KakaoToken {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;

    private String scope;
    private int refresh_token_expires_in;
}
