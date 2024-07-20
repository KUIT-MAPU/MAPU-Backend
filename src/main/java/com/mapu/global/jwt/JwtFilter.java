package com.mapu.global.jwt;

import com.mapu.global.jwt.dto.JwtUserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.mapu.global.jwt.JwtUtil.ACCESS;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

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
