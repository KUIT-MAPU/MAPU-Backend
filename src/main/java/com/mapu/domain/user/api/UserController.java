package com.mapu.domain.user.api;

import com.mapu.domain.user.api.request.SignUpRequestDTO;
import com.mapu.domain.user.api.request.UserUpdateRequestDTO;
import com.mapu.domain.user.application.UserService;
import com.mapu.domain.user.application.response.SignInResponseDTO;
import com.mapu.domain.user.application.response.SignUpResponseDTO;
import com.mapu.domain.user.application.response.UserInfoResponseDTO;
import com.mapu.domain.user.application.response.UserUpdateResponseDTO;
import com.mapu.domain.user.exception.UserException;
import com.mapu.domain.user.exception.errorcode.UserExceptionErrorCode;
import com.mapu.global.common.response.BaseResponse;
import com.mapu.infra.oauth.application.OAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
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
    public BaseResponse<SignInResponseDTO> socialLogin(@PathVariable("socialLoginType") String oauthType,
                                                       @RequestParam("code") String code,
                                                       HttpServletRequest httpServletRequest,
                                                       HttpServletResponse httpServletResponse) {
        log.info("socialLoginType: {}", oauthType.toUpperCase());
        SignInResponseDTO response = oAuthService.login(oauthType.toUpperCase(), code, httpServletRequest.getSession(), httpServletResponse);

        return new BaseResponse<>(response);
    }

    /**
     * 유저 회원가입 API
     */

    @PostMapping("/signup")
    public BaseResponse<SignUpResponseDTO> saveUser(@Validated @RequestPart("requestDTO") SignUpRequestDTO request,
                                                    @RequestPart("imageFile") MultipartFile imageFile,
                                                    HttpServletRequest httpServletRequest,
                                                    HttpServletResponse httpServletResponse) throws IOException {
        //TODO: @Validated 적용안되는 문제해결 필요
        SignUpResponseDTO response = userService.signUp(request, imageFile, httpServletRequest.getSession(false), httpServletResponse);
        return new BaseResponse<>(response);
    }

    /**
     * 유저데이터 조회 API
     */
    @GetMapping
    public BaseResponse<UserInfoResponseDTO> getUserInfo(){
        //ContextHolder로부터 userId 받기
        //UserInfoResponseDTO response = userService.getUserInfo();

        UserInfoResponseDTO response = new UserInfoResponseDTO();
        response.setMessage("유저데이터 조회 API 호출");

        return new BaseResponse<>(response);
    }

    /**
     * 유저데이터 수정 API
     */
//    @PostMapping
//    public BaseResponse<UserUpdateResponseDTO> updateUserInfo(@RequestBody @Validated UserUpdateRequestDTO request,
//                                                              MultipartFile image){
//        //ContextHolder로부터 userId 받기
//
//        UserUpdateResponseDTO response= userService.updateUser();
//        return new BaseResponse<>(response);
//    }
//}

//    @GetMapping("/logout")
//    public BaseResponse<Object> logout() {
//
//    }

}
