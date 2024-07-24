package com.mapu.infra.oauth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mapu.domain.user.application.response.SignInUpResponseDTO;
import com.mapu.domain.user.dao.UserRepository;
import com.mapu.domain.user.domain.User;
import com.mapu.domain.user.exception.UserException;
import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.dto.JwtUserDto;
import com.mapu.global.jwt.exception.JwtException;
import com.mapu.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import com.mapu.infra.oauth.domain.OAuthUserInfo;
import com.mapu.infra.oauth.exception.OAuthException;
import com.mapu.infra.oauth.exception.errorcode.OAuthExceptionErrorCode;
import com.mapu.infra.oauth.google.GoogleToken;
import com.mapu.infra.oauth.google.GoogleUserInfo;
import com.mapu.infra.oauth.google.GoogleUserService;
import com.mapu.infra.oauth.kakao.KakaoToken;
import com.mapu.infra.oauth.kakao.KakaoUserInfo;
import com.mapu.infra.oauth.kakao.KakaoUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OAuthService {
    private final GoogleUserService googleUserService;
    private final KakaoUserService kakaoUserService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public SignInUpResponseDTO login(String socialLoginType, String code, HttpSession session, HttpServletResponse response) {
        OAuthUserInfo userInfo = getUserInfoFromOAuth(socialLoginType, code);

        if (!userRepository.existsByEmail(userInfo.getEmail())) {
            //회원가입 필요
            saveUserInfoToSession(session, userInfo);
            throw new UserException(OAuthExceptionErrorCode.NEED_TO_SIGNUP);
        }

        User user = userRepository.findByEmail(userInfo.getEmail());

        //로그인 성공
        String accessToken = null;
        try {
            //jwt 발급하기
            JwtUserDto jwtUserDto = JwtUserDto.builder()
                    .name(user.getId())
                    .role(user.getRole())
                    .build();

            accessToken = jwtUtil.createAccessToken(jwtUserDto);
            response.addCookie(jwtUtil.createRefreshJwtCookie(jwtUserDto));
        } catch (Exception e) {
            throw new JwtException(JwtExceptionErrorCode.ERROR_IN_JWT);
        }

        if (accessToken == null) {
            throw new JwtException(JwtExceptionErrorCode.ERROR_IN_JWT);
        }

        SignInUpResponseDTO responseDTO = SignInUpResponseDTO.builder()
                .imgUrl(user.getImage())
                .profileId(user.getProfileId())
                .accessToken(accessToken)
                .build();

        return responseDTO;
    }

    private OAuthUserInfo getUserInfoFromOAuth(String socialLoginType, String code) {
        OAuthUserInfo userInfo = null;

        switch (socialLoginType) {
            case "GOOGLE": {
                userInfo = getGoogleUserInfo(code);
                log.info("google user info: {}", userInfo.getEmail());
                break;
            }
            case "KAKAO": {
                userInfo = getKakaoUserInfo(code);
                break;
            }
            default: {
                throw new OAuthException(OAuthExceptionErrorCode.INVALID_SOCIAL_LOGIN_TYPE);
            }
        }
        return userInfo;
    }

    private void saveUserInfoToSession(HttpSession session, OAuthUserInfo oAuthUserInfo) {
        session.setAttribute("platform_id", oAuthUserInfo.getSocialId());
        session.setAttribute("email", oAuthUserInfo.getEmail());
        session.setAttribute("platform_name", oAuthUserInfo.getSocialProvider());
        log.info("New user info saved in session: {}", session.getAttribute("email"));
    }

    private OAuthUserInfo getGoogleUserInfo(String code){
        GoogleToken googleToken = googleUserService.getAccessToken(code);
        try {
            GoogleUserInfo googleUserInfo = googleUserService.requestGoogleUserInfo(googleToken);
            log.info("googleUser: " + googleUserInfo.email);
            OAuthUserInfo oAuthUserInfo = OAuthUserInfo.builder()
                    .socialId(googleUserInfo.getId())
                    .socialProvider("google")
                    .email(googleUserInfo.email)
                    .build();
            return oAuthUserInfo;
        }catch (Exception e){
            throw new OAuthException(OAuthExceptionErrorCode.GOOGLE_LOGIN_FAIL);
        }
    }

    private OAuthUserInfo getKakaoUserInfo(String code) {
        KakaoToken kakaoToken = kakaoUserService.getAccessToken(code);
        try {
            KakaoUserInfo kakaoUserInfo = kakaoUserService.requestKakaoUserInfo(kakaoToken);
            log.info("kakaoUser: " + kakaoUserInfo.getKakao_account().getEmail());
            OAuthUserInfo oAuthUserInfo = OAuthUserInfo.builder()
                    .socialId(kakaoUserInfo.getId())
                    .socialProvider("kakao")
                    .email(kakaoUserInfo.getKakao_account().email)
                    .build();
            return oAuthUserInfo;
        }catch (Exception e){
            throw new OAuthException(OAuthExceptionErrorCode.KAKAO_LOGIN_FAIL);
        }
    }
}
