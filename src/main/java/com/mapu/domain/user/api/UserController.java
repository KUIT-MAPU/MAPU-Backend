package com.mapu.domain.user.api;

import com.mapu.domain.user.api.request.SignUpRequestDTO;
import com.mapu.domain.user.api.request.UserUpdateRequestDTO;
import com.mapu.domain.user.application.UserService;
import com.mapu.domain.user.application.response.SignInUpResponseDTO;
import com.mapu.domain.user.application.response.UserDeleteResponseDTO;
import com.mapu.domain.user.application.response.UserInfoResponseDTO;
import com.mapu.global.common.response.BaseResponse;
import com.mapu.global.jwt.dto.JwtUserDto;
import com.mapu.infra.oauth.application.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final OAuthService oAuthService;

    /**
     * 유저 로그인 API
     */

    @GetMapping("/signin/{socialLoginType}")
    public BaseResponse<SignInUpResponseDTO> socialLogin(@PathVariable("socialLoginType") String oauthType,
                                                         @RequestParam("code") String code,
                                                         HttpServletRequest httpServletRequest,
                                                         HttpServletResponse httpServletResponse) {
        log.info("socialLoginType: {}", oauthType.toUpperCase());
        SignInUpResponseDTO response = oAuthService.login(oauthType.toUpperCase(), code, httpServletRequest.getSession(), httpServletResponse);

        return new BaseResponse<>(response);
    }

    /**
     * 유저 회원가입 API
     */

    @PostMapping("/signup")
    public BaseResponse<SignInUpResponseDTO> saveUser(@Validated @RequestPart("requestDTO") SignUpRequestDTO request,
                                                    @RequestPart("imageFile") MultipartFile imageFile,
                                                    HttpServletRequest httpServletRequest,
                                                    HttpServletResponse httpServletResponse) throws IOException {
        SignInUpResponseDTO response = userService.signUp(request, imageFile, httpServletRequest.getSession(false), httpServletResponse);

        return new BaseResponse<>(response);
    }

    /**
     * 유저데이터 조회 API
     */

    @GetMapping
    public BaseResponse<UserInfoResponseDTO> getUserInfo(@AuthenticationPrincipal JwtUserDto jwtUserDto){
        UserInfoResponseDTO response = userService.getUserInfo(Long.parseLong(jwtUserDto.getName()));
        return new BaseResponse<>(response);
    }

    /**
     * 유저데이터 수정 API
     */
    @PostMapping
    public BaseResponse updateUserInfo(@AuthenticationPrincipal JwtUserDto jwtUserDto,
                                               @Validated @RequestPart("requestDTO") UserUpdateRequestDTO request,
                                               @RequestPart("imageFile") MultipartFile image) throws IOException {
        userService.updateUser(Long.parseLong(jwtUserDto.getName()), request, image);
        return new BaseResponse<>();
    }

    @PostMapping("/delete")
    public BaseResponse<UserDeleteResponseDTO> deleteUser(HttpServletRequest httpServletRequest, @RequestParam("email") String deleteUserEmail) {
        UserDeleteResponseDTO response = userService.deleteUser(httpServletRequest, deleteUserEmail);
        return new BaseResponse<>(response);
    }
}
