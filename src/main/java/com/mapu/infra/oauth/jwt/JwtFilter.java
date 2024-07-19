package com.mapu.infra.oauth.jwt;

import com.mapu.infra.oauth.jwt.dto.CustomOAuth2User;
import com.mapu.infra.oauth.jwt.dto.JwtUserDto;
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

import static com.mapu.infra.oauth.jwt.JwtUtil.ACCESS;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(ACCESS)) {
                token = cookie.getValue();
            }
        }

        //TODO access 토큰 없어도 접근 가능해야 하는 화면에서 문제가 되는지 테스트 필요
//        if (authorization == null) {
//            //token null //예외 처리 필요한지? 나머지 필터 돌려야 하는데 어떻게 예외를 터뜨릴지?
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authorization;
//        if (jwtUtil.isExpired(token)) {
//            //token Expired //refresh token 사용
//            filterChain.doFilter(request, response);
//            return;
//        }

        JwtUserDto jwtUserDto = jwtService.getUserDtoFromToken(token, ACCESS);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(jwtUserDto);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
