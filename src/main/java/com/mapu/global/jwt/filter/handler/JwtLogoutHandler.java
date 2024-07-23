package com.mapu.global.jwt.filter.handler;

import com.mapu.global.common.exception.BaseException;
import com.mapu.global.common.exception.errorcode.BaseExceptionErrorCode;
import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.application.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
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
}
