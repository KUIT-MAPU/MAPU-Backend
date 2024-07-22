package com.mapu.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapu.global.common.exception.BaseException;
import com.mapu.global.common.exception.errorcode.BaseExceptionErrorCode;
import com.mapu.global.common.response.status.ResponseStatus;
import com.mapu.global.jwt.application.JwtService;
import com.mapu.global.jwt.dto.JwtUserDto;
import com.mapu.global.jwt.exception.JwtException;
import com.mapu.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.mapu.global.jwt.JwtUtil.ACCESS;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    //TODO: 필터에서 발생한 에러 핸들링 코드 구현 필요
    //TODO: ACCESS-TOKEN을 넘겨도 403 FORBIDDEN ERROR가 뜸.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(ACCESS)) {
                log.info("jwt : {} ", cookie.getValue());
                token = cookie.getValue();
            }
        }

        if (token == null) {
            //token null //TODO 예외 처리 필요한지? 나머지 필터 돌려야 하는데 어떻게 예외를 터뜨릴지?
            filterChain.doFilter(request, response);
            return;
        }
//
//        String token = authorization;
//        if (jwtUtil.isExpired(token)) {
//            //token Expired //refresh token 사용
//            filterChain.doFilter(request, response);
//            return;
//        }

        JwtUserDto jwtUserDto = jwtService.getUserDtoFromToken(token, ACCESS);
        Authentication authToken = new UsernamePasswordAuthenticationToken(jwtUserDto, null, jwtUserDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

}