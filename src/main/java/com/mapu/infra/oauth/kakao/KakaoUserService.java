package com.mapu.infra.oauth.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapu.infra.oauth.config.OAuthClientConfig;
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
            log.error("json processing error", e);
            e.printStackTrace();
        }

        return kakaoToken;
    }
    public KakaoUserInfo requestKakaoUserInfo(String token) {

        //Http 요청
        WebClient wc = WebClient.create(UserInfoUri);
        String response = wc.post()
                .uri(UserInfoUri)
                .header("Authorization", "Bearer " + token)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoUserInfo kakaoUserInfo = null;

        try {
            kakaoUserInfo = objectMapper.readValue(response, KakaoUserInfo.class);
        } catch (JsonProcessingException e) {
            log.error("json processing error", e);
            e.printStackTrace();
        }
        return kakaoUserInfo;
    }

    // 소은이가 하는 부분
//    @Transactional
//    public User saveUser(String access_token) {
//        KakaoUserInfo kakaoUserInfo = findKakaoUser(access_token); //사용자 정보 받아오기
//         User user = userRepository.findByUserid(kakaoUserInfo.getId());
//
//        //처음이용자 강제 회원가입
//        if(user ==null) {
//            user = User.builder()
//                    .userid(kakaoUserInfo.getId())
//                    .password(null) //필요없으니 일단 아무거도 안넣음. 원하는데로 넣으면 됌
//                    .nickname(kakaoUserInfo.getKakao_account().getProfile().getNickname())
//                    .profileImg(kakaoUserInfo.getKakao_account().getProfile().getProfile_image_url())
//                    .email(kakaoUserInfo.getKakao_account().getEmail())
//                    .roles("USER")
//                    .createTime(LocalDateTime.now())
//                    .provider("Kakao")
//                    .build();
//
//            userRepository.save(user);
//        }
//
//        return user;
//    }
}
