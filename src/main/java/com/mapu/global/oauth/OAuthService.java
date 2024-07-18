package com.mapu.global.oauth;

import com.mapu.global.common.exception.BaseException;
import com.mapu.global.common.exception.errorcode.BaseExceptionErrorCode;
import com.mapu.global.oauth.google.GoogleToken;
import com.mapu.global.oauth.google.GoogleUserInfo;
import com.mapu.global.oauth.google.GoogleUserService;
import com.mapu.global.oauth.kakao.KakaoToken;
import com.mapu.global.oauth.kakao.KakaoUserInfo;
import com.mapu.global.oauth.kakao.KakaoUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {

    private final GoogleUserService googleUserService;
    private final KakaoUserService kakaoUserService;

    public OAuthUserInfo oAuthLogin(String socialLoginType, String code) {
        OAuthUserInfo userInfo = null;
        switch (socialLoginType) {
            case "GOOGLE": {
                try {
                    GoogleToken oAuthToken = googleUserService.getAccessToken(code);
                    GoogleUserInfo googleUserInfo = googleUserService.requestGoogleUserInfo(oAuthToken);
                    log.info("googleUser: " + googleUserInfo.email);
                    userInfo = new OAuthUserInfo(googleUserInfo.email, socialLoginType);
                    break;
                } catch (Exception e) {
                    throw new BaseException("Google social login error", BaseExceptionErrorCode.BAD_REQUEST);
                }finally {
                    break;
                }
            }
            case "KAKAO": {
                try {
                    KakaoToken oAuthToken = kakaoUserService.getAccessToken(code);
                    KakaoUserInfo kakaoUserInfo = kakaoUserService.requestKakaoUserInfo(oAuthToken.getAccess_token());
                    // KakaoUserInfo kakaoUserInfo = kakaoUserService.requestKakaoUserInfo(code); // postman test 부분
                    log.info("kakaoUser: " + kakaoUserInfo.getKakao_account().getEmail());
                    userInfo = new OAuthUserInfo(kakaoUserInfo.getKakao_account().getEmail(), socialLoginType);
                    break;
                } catch (Exception e) {
                    throw new BaseException("Kakao social login error", BaseExceptionErrorCode.BAD_REQUEST);
                } finally {
                    break;
                }
            }
            default: {
                // 기본 오류 처리
                throw new BaseException("This is invalid social login type", BaseExceptionErrorCode.BAD_REQUEST);
            }
        }
        return userInfo;
    }
}
