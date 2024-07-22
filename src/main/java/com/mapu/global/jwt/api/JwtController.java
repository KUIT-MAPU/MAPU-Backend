package com.mapu.global.jwt.api;

import com.mapu.global.common.exception.BaseException;
import com.mapu.global.common.exception.errorcode.BaseExceptionErrorCode;
import com.mapu.global.common.response.BaseResponse;
import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.application.JwtService;
import com.mapu.global.jwt.dto.JwtUserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/jwt")
@RequiredArgsConstructor
@RestController
public class JwtController {
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    static String SUCCESS_MESSAGE = "access token 재발급 성공";

    @PostMapping("/reissue")
    public BaseResponse<String> reissue(HttpServletRequest request, HttpServletResponse response) {
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

        JwtUserDto jwtUserDto = jwtService.getUserDtoFromToken(refresh, JwtUtil.REFRESH);
        response.addCookie(jwtUtil.createAccessJwtCookie(jwtUserDto));
        response.addCookie(jwtUtil.createRefreshJwtCookie(jwtUserDto));

        return new BaseResponse<>(SUCCESS_MESSAGE);
    }
}