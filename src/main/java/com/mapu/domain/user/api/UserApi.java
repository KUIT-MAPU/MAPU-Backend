package com.mapu.domain.user.api;

import com.mapu.domain.user.api.request.SignUpRequestDTO;
import com.mapu.domain.user.application.UserService;
import com.mapu.domain.user.application.response.SignUpResponseDTO;
import com.mapu.domain.user.application.response.UserDTO;
import com.mapu.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    @PostMapping("/signup")
    public BaseResponse<SignUpResponseDTO> registerUser(@RequestBody @Validated SignUpRequestDTO signUpRequestDTO) {
        SignUpResponseDTO signUpResponseDTO = userService.signUp(signUpRequestDTO);



        //JWT Token 반환 해주세요
        return new BaseResponse<>(signUpResponseDTO);
    }
}

//    @GetMapping("/logout")
//    public BaseResponse<Object> logout() {
//
//    }
