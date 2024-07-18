package com.mapu.infra.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OAuthUserInfo {
    String email;
    String socialProvider;
}
