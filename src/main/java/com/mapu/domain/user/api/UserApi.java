package com.mapu.domain.user.api;

import com.mapu.domain.user.api.request.SignUpRequestDTO;
import com.mapu.domain.user.api.request.UserUpdateRequestDTO;
import com.mapu.domain.user.application.UserService;
import com.mapu.domain.user.application.response.SignUpResponseDTO;
import com.mapu.domain.user.application.response.UserInfoResponseDTO;
import com.mapu.domain.user.application.response.UserUpdateResponseDTO;
import com.mapu.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    /**
     * 유저 회원가입 API
     */

    @PostMapping("/signup")
    public BaseResponse<SignUpResponseDTO> saveUser(@RequestBody @Validated SignUpRequestDTO request) {
        SignUpResponseDTO response = userService.signUp(request);

        //JWT Token 반환 해주세요
        return new BaseResponse<>(response);
    }

    /**
     * 유저데이터 조회 API
     */
    @GetMapping
    public BaseResponse<UserInfoResponseDTO> getUserInfo(){
        //ContextHolder로부터 userId 받기

        UserInfoResponseDTO response = userService.getUserInfo();
        return new BaseResponse<>(response);
    }

    /**
     * 유저데이터 수정 API
     */
    @PostMapping
    public BaseResponse<UserUpdateResponseDTO> updateUserInfo(@RequestBody @Validated UserUpdateRequestDTO request,
                                                              MultipartFile image){
        //ContextHolder로부터 userId 받기

        UserUpdateResponseDTO response= userService.updateUser();
        return new BaseResponse<>(response);
    }
}

//    @GetMapping("/logout")
//    public BaseResponse<Object> logout() {
//
//    }
