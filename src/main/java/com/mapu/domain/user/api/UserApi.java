package com.mapu.domain.user.api;

import com.mapu.domain.user.api.request.SignUpRequestDTO;
import com.mapu.domain.user.application.UserService;
import com.mapu.domain.user.application.response.SignUpResponseDTO;
import com.mapu.domain.user.application.response.UserDTO;
import com.mapu.global.common.response.BaseResponse;
import com.mapu.infra.oauth.jwt.JwtUtil;
import com.mapu.infra.oauth.jwt.dto.JwtUserDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public BaseResponse<SignUpResponseDTO> registerUser(@RequestBody @Validated SignUpRequestDTO signUpRequestDTO,
                                                        HttpServletResponse response) {
        SignUpResponseDTO signUpResponseDTO = userService.signUp(signUpRequestDTO);



        //TODO: 유저 role, email 값 받아와서 넣기
        String role = "ROLE_USER"; //임시 role
        String email = "email"; //임시 이메일

        JwtUserDto jwtUserDto = new JwtUserDto();
        jwtUserDto.setName(email);
        jwtUserDto.setRole(role);
        response.addCookie(jwtUtil.createAccessJwtCookie(jwtUserDto));
        response.addCookie(jwtUtil.createRefreshJwtCookie(jwtUserDto));

        return new BaseResponse<>(signUpResponseDTO);
    }
}

//    @GetMapping("/logout")
//    public BaseResponse<Object> logout() {
//
//    }
