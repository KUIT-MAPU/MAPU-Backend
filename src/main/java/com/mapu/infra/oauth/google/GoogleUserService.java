package com.mapu.infra.oauth.google;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapu.infra.oauth.config.OAuthClientConfig;
import com.mapu.infra.oauth.exception.OAuthException;
import com.mapu.infra.oauth.exception.errorcode.OAuthExceptionErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleUserService {

    private final OAuthClientConfig oAuthClientConfig;

    private final String accessTokenUri = "https://oauth2.googleapis.com/token";
    private final String UserInfoUri = "https://www.googleapis.com/oauth2/v1/userinfo";


    public GoogleToken getAccessToken(String code) {
        //요청 param (body)
        MultiValueMap<String , String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", oAuthClientConfig.getGoogleClientId());
        params.add("redirect_uri", oAuthClientConfig.getGoogleRedirectUri());
        params.add("code", code);
        params.add("client_secret", oAuthClientConfig.getGoogleClientSecret());


        //request
        WebClient wc = WebClient.create(accessTokenUri);
        String response = wc.post()
                .uri(accessTokenUri)
                .body(BodyInserters.fromFormData(params))
                .header("Content-type","application/x-www-form-urlencoded;charset=utf-8" ) //요청 헤더
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //json형태로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleToken googleToken =null;

        try {
            googleToken = objectMapper.readValue(response, GoogleToken.class);
        } catch (JsonProcessingException e) {
            throw new OAuthException(OAuthExceptionErrorCode.GET_TOKEN_FAIL);
        }
        log.info("Google Token: {}", googleToken.getAccess_token());
        return googleToken;
    }

    public GoogleUserInfo requestGoogleUserInfo(GoogleToken googleToken) throws JsonProcessingException {
        //Http 요청
        log.info("requestGoogleUserInfo");
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

        log.info("response: {}", response);
        googleUserInfo = objectMapper.readValue(response, GoogleUserInfo.class);
        return googleUserInfo;
    }
}
