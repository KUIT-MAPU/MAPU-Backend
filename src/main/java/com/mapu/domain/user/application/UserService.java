package com.mapu.domain.user.application;

import com.mapu.domain.user.api.request.SignUpRequestDTO;
import com.mapu.domain.user.application.response.SignUpResponseDTO;
import com.mapu.domain.user.dao.UserRepository;
import com.mapu.domain.user.domain.User;
import com.mapu.global.common.exception.UserException;
import com.mapu.global.common.exception.errorcode.UserExceptionErrorCode;
import com.mapu.domain.user.application.response.UserDTO;
import com.mapu.domain.user.domain.UserRole;
import com.mapu.global.common.response.BaseResponse;
import com.mapu.infra.oauth.OAuthService;
import com.mapu.infra.oauth.dao.OAuthRepository;
import com.mapu.infra.oauth.domain.OAuth;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OAuthRepository oAuthRepository;
    @Autowired
    private HttpSession session;


    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {

        // signUpRequestDTO 입력값 검증
        validateSignUpRequest(signUpRequestDTO);
        // 세션에 값이 있는지 확인
        //TODO: 세션 여기 추가
        String platfrom_name ="";
        String platform_id = "";
        String email = "";

        if (platfrom_name == null || platform_id == null || email ==null) {
            throw new UserException(UserExceptionErrorCode.LOGOUT_FAIL);
        }

        // 닉네임 중복 검사
        if (userRepository.existsByNickname(signUpRequestDTO.getNickname())) {
            throw new UserException(UserExceptionErrorCode.DUPLICATE_NICKNAME);
        }

        // 프로필 ID 중복 검사
        if (userRepository.existsByProfileId(signUpRequestDTO.getProfile_id())) {
            throw new UserException(UserExceptionErrorCode.DUPLICATE_PROFILE_ID);
        }

        User user = User.createUser(
                email,
                "USER",
                signUpRequestDTO.getNickname(),
                signUpRequestDTO.getProfile_id(),
                signUpRequestDTO.getImage(),
                "ACTIVE"
        );


        userRepository.save(user);

        OAuth oAuth = OAuth.createOAuth(
                platfrom_name,
                platform_id,
                user
        );

        oAuthRepository.save(oAuth);

        // 세션에 있는 유저 정보 삭제
        session.removeAttribute("platfrom_name");
        session.removeAttribute("platfrom_id");
        session.removeAttribute("email");

        return new SignUpResponseDTO();
    }

    private void validateSignUpRequest(SignUpRequestDTO signUpRequestDTO) {
        if (signUpRequestDTO.getNickname() == null || signUpRequestDTO.getNickname().trim().isEmpty()) {
            throw new UserException(UserExceptionErrorCode.INVALID_NICKNAME);
        }
        if (signUpRequestDTO.getNickname().length() < 3 || signUpRequestDTO.getNickname().length() > 12) {
            throw new UserException(UserExceptionErrorCode.INVALID_NICKNAME_LENGTH);
        }
        if (signUpRequestDTO.getProfile_id() == null || signUpRequestDTO.getProfile_id().trim().isEmpty()) {
            throw new UserException(UserExceptionErrorCode.INVALID_PROFILE_ID);
        }
        if (signUpRequestDTO.getProfile_id().length() < 3 || signUpRequestDTO.getProfile_id().length() > 20) {
            throw new UserException(UserExceptionErrorCode.INVALID_PROFILE_ID_LENGTH);
        }
    }

//    @Transactional
//    public User getUserByEmail(String email) {
//        return userRepository.findByEmail(email)
//    }

}