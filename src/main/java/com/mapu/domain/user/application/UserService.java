package com.mapu.domain.user.application;

import com.mapu.domain.user.api.request.SignUpRequestDTO;
import com.mapu.domain.user.application.response.SignUpResponseDTO;
import com.mapu.domain.user.dao.UserRepository;
import com.mapu.domain.user.domain.User;
import com.mapu.domain.user.exception.UserException;
import com.mapu.domain.user.exception.errorcode.UserExceptionErrorCode;
import com.mapu.infra.oauth.dao.OAuthRepository;
import com.mapu.infra.oauth.domain.OAuth;
import com.mapu.infra.oauth.domain.OAuthUserInfo;
import com.mapu.infra.s3.application.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OAuthRepository oAuthRepository;
    private final S3Service s3Service;

    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO, MultipartFile imageFile, HttpSession session) throws IOException {
        OAuthUserInfo userInfo = getUserInfoFromSession(session);
        checkDuplicateSignUpRequest(userInfo.getEmail());
        log.info("isDuplicate?: {}", userRepository.existsByEmail(userInfo.getEmail()));
        checkDuplicateNameOrId(signUpRequestDTO);
        String imageUrl = uploadImage(imageFile);
        saveDataToDB(userInfo, signUpRequestDTO, imageUrl);
        removeSessionData(session);
        //TODO: JWT 토큰 발급

        return new SignUpResponseDTO(imageUrl);
    }

    private void checkDuplicateSignUpRequest(String email) {
        if(userRepository.existsByEmail(email))
            throw new UserException(UserExceptionErrorCode.ALREADY_SIGNUP);
    }

    private String uploadImage(MultipartFile imageFile) throws IOException {
        if(imageFile.isEmpty())
            return null;

        return s3Service.uploadImage(imageFile);
    }

    private void saveDataToDB(OAuthUserInfo userInfo, SignUpRequestDTO signUpRequestDTO, String imageUrl) {
        User user = User.builder()
                .email(userInfo.getEmail())
                .image(imageUrl)
                .nickname(signUpRequestDTO.getNickname())
                .profileId(signUpRequestDTO.getProfileId())
                .role("USER")
                .status("ACTIVE")
                        .build();

        userRepository.save(user);

        OAuth oAuth = OAuth.builder().platformId(userInfo.getSocialId())
                .platformName(userInfo.getSocialProvider())
                .user(user)
                .build();

        oAuthRepository.save(oAuth);
    }

    private void removeSessionData(HttpSession session) {
        session.removeAttribute("platform_name");
        session.removeAttribute("platform_id");
        session.removeAttribute("email");
    }

    private OAuthUserInfo getUserInfoFromSession(HttpSession session) {
        if(session==null){
            throw new UserException(UserExceptionErrorCode.NO_SESSION);
        }
        OAuthUserInfo userInfo = OAuthUserInfo.builder()
                .socialId(session.getAttribute("platform_id").toString())
                .socialProvider(session.getAttribute("platform_name").toString())
                .email(session.getAttribute("email").toString())
                .build();
        isExistInfoInSession(userInfo);

        return userInfo;
    }

    private void isExistInfoInSession(OAuthUserInfo userInfo) {
        //세션에 값이 있는지 확인
        if (userInfo.getEmail() == null ||
                userInfo.getSocialId() == null ||
                userInfo.getSocialProvider() ==null) {
            throw new UserException(UserExceptionErrorCode.NO_INFO_IN_SESSION);
        }
    }

    private void checkDuplicateNameOrId(SignUpRequestDTO signUpRequestDTO) {
        // 닉네임 중복 검사
        if (userRepository.existsByNickname(signUpRequestDTO.getNickname())) {
            throw new UserException(UserExceptionErrorCode.DUPLICATE_NICKNAME);
        }
        // 프로필 ID 중복 검사
        if (userRepository.existsByProfileId(signUpRequestDTO.getProfileId())) {
            throw new UserException(UserExceptionErrorCode.DUPLICATE_PROFILE_ID);
        }
    }

}