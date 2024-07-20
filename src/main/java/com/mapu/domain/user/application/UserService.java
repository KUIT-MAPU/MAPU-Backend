package com.mapu.domain.user.application;

import com.mapu.domain.user.api.request.SignUpRequestDTO;
import com.mapu.domain.user.application.response.SignUpResponseDTO;
import com.mapu.domain.user.dao.UserRepository;
import com.mapu.domain.user.domain.User;
import com.mapu.global.common.exception.UserException;
import com.mapu.global.common.exception.errorcode.UserExceptionErrorCode;
import com.mapu.global.jwt.dto.JwtUserDto;
import com.mapu.infra.oauth.dao.OAuthRepository;
import com.mapu.infra.oauth.domain.OAuth;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final OAuthRepository oAuthRepository;
    private final HttpSession session;

    public JwtUserDto getJwtUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
        {
            return null;
        }
        JwtUserDto jwtUserDto = new JwtUserDto();
        jwtUserDto.setName(user.getEmail()); //TODO jwt에는 여기서만 넣는거라 Email 말고 다른 id값으로 넣어도 되는데 뭐가 나을지?
        jwtUserDto.setRole(user.getRole());
        return jwtUserDto;
    }

    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {

        // signUpRequestDTO 입력값 검증
        validateSignUpRequest(signUpRequestDTO);

        //세션 값 가져오기
        String platfromName = session.getAttribute("platfromName").toString();
        String platformId = session.getAttribute("platformId").toString();
        String email = session.getAttribute("email").toString();

        //세션에 값이 있는지 확인
        if (platfromName == null || platformId == null || email ==null) {
            throw new UserException(UserExceptionErrorCode.LOGOUT_FAIL);
        }

        // 닉네임 중복 검사
        if (userRepository.existsByNickname(signUpRequestDTO.getNickname())) {
            throw new UserException(UserExceptionErrorCode.DUPLICATE_NICKNAME);
        }

        // 프로필 ID 중복 검사
        if (userRepository.existsByProfileId(signUpRequestDTO.getProfileId())) {
            throw new UserException(UserExceptionErrorCode.DUPLICATE_PROFILE_ID);
        }

        User user = User.createUser(
                email,
                "USER",
                signUpRequestDTO.getNickname(),
                signUpRequestDTO.getProfileId(),
                signUpRequestDTO.getImage(),
                "ACTIVE"
        );


        userRepository.save(user);

        OAuth oAuth = OAuth.createOAuth(
                platfromName,
                platformId,
                user
        );

        oAuthRepository.save(oAuth);

        // 세션에 있는 유저 정보 삭제
        session.removeAttribute("platformName");
        session.removeAttribute("platformId");
        session.removeAttribute("email");

        SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO();
        signUpResponseDTO.setEmail(email);
        return signUpResponseDTO;
    }

    private void validateSignUpRequest(SignUpRequestDTO signUpRequestDTO) {
        if (signUpRequestDTO.getNickname() == null || signUpRequestDTO.getNickname().trim().isEmpty()) {
            throw new UserException(UserExceptionErrorCode.INVALID_NICKNAME);
        }
        if (signUpRequestDTO.getNickname().length() < 3 || signUpRequestDTO.getNickname().length() > 12) {
            throw new UserException(UserExceptionErrorCode.INVALID_NICKNAME_LENGTH);
        }
        if (signUpRequestDTO.getProfileId() == null || signUpRequestDTO.getProfileId().trim().isEmpty()) {
            throw new UserException(UserExceptionErrorCode.INVALID_PROFILE_ID);
        }
        if (signUpRequestDTO.getProfileId().length() < 3 || signUpRequestDTO.getProfileId().length() > 20) {
            throw new UserException(UserExceptionErrorCode.INVALID_PROFILE_ID_LENGTH);
        }
    }

}