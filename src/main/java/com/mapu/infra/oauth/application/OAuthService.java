package com.mapu.infra.oauth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mapu.domain.user.application.response.SignInResponseDTO;
import com.mapu.domain.user.dao.UserRepository;
import com.mapu.domain.user.exception.UserException;
import com.mapu.domain.user.exception.errorcode.UserExceptionErrorCode;
import com.mapu.global.common.exception.BaseException;
import com.mapu.global.common.exception.errorcode.BaseExceptionErrorCode;
import com.mapu.infra.oauth.domain.OAuth;
import com.mapu.infra.oauth.domain.OAuthUserInfo;
import com.mapu.infra.oauth.exception.OAuthException;
import com.mapu.infra.oauth.exception.errorcode.OAuthExceptionErrorCode;
import com.mapu.infra.oauth.google.GoogleToken;
import com.mapu.infra.oauth.google.GoogleUserInfo;
import com.mapu.infra.oauth.google.GoogleUserService;
import com.mapu.infra.oauth.kakao.KakaoToken;
import com.mapu.infra.oauth.kakao.KakaoUserInfo;
import com.mapu.infra.oauth.kakao.KakaoUserService;
import jakarta.servlet.http.HttpServletRequest;
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
    static final String LOGIN_SUCCESS_MESSAGE = "로그인에 성공하였습니다.";

    public SignInResponseDTO login(String socialLoginType, String code, HttpSession session) {
        OAuthUserInfo userInfo = getUserInfoFromOAuth(socialLoginType, code);

        if(userRepository.existsByEmail(userInfo.getEmail())){
            //로그인 성공
            //TODO: JWT 토큰 발급
        }else{
            saveUserInfoToSession(session,userInfo);
            throw new UserException(OAuthExceptionErrorCode.NEED_TO_SIGNUP);
        }

        return new SignInResponseDTO(LOGIN_SUCCESS_MESSAGE);
    }

    private OAuthUserInfo getUserInfoFromOAuth(String socialLoginType, String code) {
        OAuthUserInfo userInfo = null;

        switch (socialLoginType) {
            case "GOOGLE": {
                try {
                    userInfo = getGoogleUserInfo(code);
                    //TODO: null일 때 예외 처리
                } catch (Exception e) {
                    throw new OAuthException(OAuthExceptionErrorCode.GOOGLE_LOGIN_FAIL);
                }finally {
                    break;
                }
            }
            case "KAKAO": {
                try {
                    userInfo = getKakaoUserInfo(code);
                    //TODO: null일 때 예외 처리
                } catch (Exception e) {
                    throw new OAuthException(OAuthExceptionErrorCode.KAKAO_LOGIN_FAIL);
                } finally {
                    break;
                }
            }
            default: {
                // 기본 오류 처리
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

    private OAuthUserInfo getGoogleUserInfo(String code) throws JsonProcessingException {
        GoogleToken oAuthToken = googleUserService.getAccessToken(code);
        GoogleUserInfo googleUserInfo = googleUserService.requestGoogleUserInfo(oAuthToken);
        log.info("googleUser: " + googleUserInfo.email);

        OAuthUserInfo oAuthUserInfo = OAuthUserInfo.builder()
                .socialId(googleUserInfo.getId())
                .socialProvider("google")
                .email(googleUserInfo.email)
                .build();

        return oAuthUserInfo;
    }

    private OAuthUserInfo getKakaoUserInfo(String code) {
        KakaoToken oAuthToken = kakaoUserService.getAccessToken(code);
        KakaoUserInfo kakaoUserInfo = kakaoUserService.requestKakaoUserInfo(oAuthToken.getAccess_token());
        //KakaoUserInfo kakaoUserInfo = kakaoUserService.requestKakaoUserInfo(code); // postman test 부분
        log.info("kakaoUser: " + kakaoUserInfo.getKakao_account().getEmail());

        OAuthUserInfo oAuthUserInfo = OAuthUserInfo.builder()
                .socialId(kakaoUserInfo.getId())
                .socialProvider("kakao")
                .email(kakaoUserInfo.getKakao_account().email)
                .build();
        
        return oAuthUserInfo;
    }
}
