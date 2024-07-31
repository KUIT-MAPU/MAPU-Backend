package com.mapu.infra.oauth.kakao;

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
public class KakaoUserService {

    private final OAuthClientConfig oAuthClientConfig;

    private final String accessTokenUri = "https://kauth.kakao.com/oauth/token";
    private final String UserInfoUri = "https://kapi.kakao.com/v2/user/me";
    private final String UnlinkUserInfoUri = "https://kapi.kakao.com";

    public KakaoToken getAccessToken(String code) {
        //요청 param (body)
        MultiValueMap<String , String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", oAuthClientConfig.getKakaoClientId());
        params.add("redirect_uri", oAuthClientConfig.getKakaoRedirectUri());
        params.add("code", code);
        params.add("client_secret", oAuthClientConfig.getKakaoClientSecret());


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
        KakaoToken kakaoToken =null;

        try {
            kakaoToken = objectMapper.readValue(response, KakaoToken.class);
        } catch (JsonProcessingException e) {
            throw new OAuthException(OAuthExceptionErrorCode.GET_TOKEN_FAIL);
        }

        return kakaoToken;
    }
    public KakaoUserInfo requestKakaoUserInfo(KakaoToken kakaoToken) throws JsonProcessingException {

        //Http 요청
        WebClient wc = WebClient.create(UserInfoUri);
        String response = wc.post()
                .uri(UserInfoUri)
                .header("Authorization", "Bearer " + kakaoToken.getAccess_token())
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoUserInfo kakaoUserInfo = null;

        kakaoUserInfo = objectMapper.readValue(response, KakaoUserInfo.class);
        return kakaoUserInfo;
    }

    public void unlinkUserInfo(long deleteOAuthId) {
        WebClient wc = WebClient.create(UnlinkUserInfoUri);
        String response = wc.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/user/unlink")
                        .queryParam("target_id_type", "user_id")
                        .queryParam("target_id", deleteOAuthId)
                        .build())
                .header("Authorization", "KakaoAK " + oAuthClientConfig.getKakaoAdminKey())
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("kakaoUnlinkUserInfo Response: {}", response);
    }
}
