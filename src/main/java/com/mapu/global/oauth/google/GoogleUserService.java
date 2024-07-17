package com.mapu.global.oauth.google;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapu.global.oauth.kakao.KakaoToken;
import com.mapu.global.oauth.kakao.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mapu.global.oauth.OAuthClientConfig;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleUserService {

    private final OAuthClientConfig oAuthClientConfig;
    private final ObjectMapper objectMapper;

    private final String UserInfoUri = "https://www.googleapis.com/oauth2/v1/userinfo";


    public GoogleToken getAccessToken(String code) throws JsonProcessingException {
        GoogleToken googleToken = objectMapper.readValue(code, GoogleToken.class);
        return googleToken;
    }

    public GoogleUserInfo requestGoogleUserInfo(GoogleToken googleToken) {
        //Http 요청
        WebClient wc = WebClient.create(UserInfoUri);
        String response = wc.get()
                .uri(UserInfoUri)
                .header("Authorization", "Bearer " + googleToken.getAccess_token())
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleUserInfo googleUserInfo = null;

        try {
            googleUserInfo = objectMapper.readValue(response, GoogleUserInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return googleUserInfo;
    }
}
