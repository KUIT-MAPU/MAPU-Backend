package com.mapu.domain.user.application;

import com.mapu.domain.follow.dao.FollowRepository;
import com.mapu.domain.map.dao.MapRepository;
import com.mapu.domain.user.api.request.SignUpRequestDTO;
import com.mapu.domain.user.api.request.UserUpdateRequestDTO;
import com.mapu.domain.user.application.response.SignInUpResponseDTO;
import com.mapu.domain.user.application.response.UserInfoResponseDTO;
import com.mapu.domain.user.dao.UserRepository;
import com.mapu.domain.user.domain.User;
import com.mapu.domain.user.domain.UserRole;
import com.mapu.domain.user.domain.UserStatus;
import com.mapu.domain.user.exception.UserException;
import com.mapu.domain.user.exception.errorcode.UserExceptionErrorCode;
import com.mapu.global.common.exception.BaseException;
import com.mapu.global.common.exception.errorcode.BaseExceptionErrorCode;
import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.application.JwtService;
import com.mapu.global.jwt.dto.JwtUserDto;
import com.mapu.infra.oauth.dao.OAuthRepository;
import com.mapu.infra.oauth.domain.OAuth;
import com.mapu.infra.oauth.domain.OAuthUserInfo;
import com.mapu.infra.s3.application.S3Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OAuthRepository oAuthRepository;
    private final S3Service s3Service;
    private final JwtService jwtService;
    private final JwtUtil jwtUtil;
    private final MapRepository mapRepository;
    private final FollowRepository followRepository;
    private static final String ANONYMOUS_NICKNAME = "환영해요!";
    private static final String ANONYMOUS_PROFILEID = "로그인이 필요해요";

    public SignInUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO, MultipartFile imageFile, HttpSession session, HttpServletResponse response) throws IOException {
        //세션으로부터 사용자 정보 받아오기
        OAuthUserInfo userInfo = getUserInfoFromSession(session);
        //이미 회원가입된 상태인지 확인하기
        checkDuplicateSignUpRequest(userInfo.getEmail());
        //닉네임/ID 중복 확인하기
        checkDuplicateNameOrId(signUpRequestDTO);
        //이미지 s3에 업로드하기
        String imageUrl = uploadImage(imageFile);
        //DB에 사용자 정보 저장하기
        saveDataToDB(userInfo, signUpRequestDTO, imageUrl);
        //세션 정보 삭제하기
        removeSessionData(session);
        //jwt 발급하기
        User user = userRepository.findByEmail(userInfo.getEmail());
        JwtUserDto jwtUserDto = null;
        try {
            jwtUserDto = JwtUserDto.builder()
                    .name(user.getId())
                    .role(user.getRole())
                    .build();
        } catch (Exception e) {
            throw new UserException(UserExceptionErrorCode.SIGNUP_FAIL);
        }

        response.addCookie(jwtUtil.createRefreshJwtCookie(jwtUserDto));
        SignInUpResponseDTO responseDTO = SignInUpResponseDTO.builder()
                .imgUrl(imageUrl)
                .profileId(signUpRequestDTO.getProfileId())
                .accessToken(jwtUtil.createAccessToken(jwtUserDto))
                .build();

        return responseDTO;
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
                .role(UserRole.USER)
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
        try{
            OAuthUserInfo userInfo = OAuthUserInfo.builder()
                    .socialId(session.getAttribute("platform_id").toString())
                    .socialProvider(session.getAttribute("platform_name").toString())
                    .email(session.getAttribute("email").toString())
                    .build();

            return userInfo;
        } catch (Exception e){
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

    public long deleteUser(HttpServletRequest request, long deleteUserId) {
        log.info("delete user id {}", deleteUserId);
        User user = userRepository.findById(deleteUserId);
        logoutUser(request);
        // userRepository.delete(user); // Option1: 완전 삭제
        user.setStatus(String.valueOf(UserStatus.DELETE)); // Option2: 상태 변경
        userRepository.save(user);
        return user.getId();
    }

    public void logoutUser(HttpServletRequest request) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BaseException(BaseExceptionErrorCode.NO_COOKIE);
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtUtil.REFRESH)) {
                refresh = cookie.getValue();
            }
        }
        jwtService.deleteRefreshJwt(refresh);
    }

    public UserInfoResponseDTO getUserInfo(JwtUserDto jwtUserDto) {
        if(jwtUserDto==null){
            return returnAnonyMousUserData();
        }

        long userId = Long.parseLong(jwtUserDto.getName());
        User findUser= userRepository.findById(userId);
        if(findUser==null) throw new UserException(UserExceptionErrorCode.INVALID_USERID);

        int mapCnt = mapRepository.countMapsByUserId(userId);
        int followerCnt = followRepository.countFollowerByUserId(userId);
        int followingCnt = followRepository.countFollowingByUserId(userId);

        UserInfoResponseDTO response = UserInfoResponseDTO
                .builder().nickname(findUser.getNickname())
                .profileId(findUser.getProfileId())
                .imgUrl(findUser.getImage())
                .mapCnt(mapCnt)
                .followerCnt(followerCnt)
                .followingCnt(followingCnt)
                .build();

        return response;
    }

    private UserInfoResponseDTO returnAnonyMousUserData() {
        UserInfoResponseDTO response = UserInfoResponseDTO
                .builder().nickname(ANONYMOUS_NICKNAME)
                .profileId(ANONYMOUS_PROFILEID)
                .imgUrl(null)
                .mapCnt(0)
                .followerCnt(0)
                .followingCnt(0)
                .build();

        return response;
    }

    public void updateUser(long userId, UserUpdateRequestDTO request, MultipartFile image) throws IOException {
        //imageUrl이 들어온 경우 -> 원래 이미지 사용
        //imageUrl이 안들어온 경우 -> 기본 이미지 혹은 새로운 이미지 파일 업로드

        User user = userRepository.findById(userId);
        if(user==null) throw new UserException(UserExceptionErrorCode.INVALID_USERID);

        //이상한 imageUrl이 들어온 경우
        if(request.getImageUrl()!=null && !request.getImageUrl().equals(user.getImage())) throw new UserException(UserExceptionErrorCode.INVALID_IMAGE);

        user.setNickname(request.getNickname());
        user.setProfileId(request.getProfileId());
        if(image.isEmpty()) user.setImage(request.getImageUrl());
        else {
            String imageUrl = uploadImage(image);
            user.setImage(imageUrl);
        }
    }

    public UserInfoResponseDTO getOtherUserInfo(JwtUserDto jwtUserDto, long otherUserId) {
        if(jwtUserDto==null){
            return returnAnonyMousUserData();
        }

        User findUser= userRepository.findById(otherUserId);
        if(findUser==null) throw new UserException(UserExceptionErrorCode.INVALID_USERID);

        int mapCnt = mapRepository.countMapsByUserId(otherUserId);
        int followerCnt = followRepository.countFollowerByUserId(otherUserId);
        int followingCnt = followRepository.countFollowingByUserId(otherUserId);

        UserInfoResponseDTO response = UserInfoResponseDTO
                .builder().nickname(findUser.getNickname())
                .profileId(findUser.getProfileId())
                .imgUrl(findUser.getImage())
                .mapCnt(mapCnt)
                .followerCnt(followerCnt)
                .followingCnt(followingCnt)
                .build();

        return response;
    }
}