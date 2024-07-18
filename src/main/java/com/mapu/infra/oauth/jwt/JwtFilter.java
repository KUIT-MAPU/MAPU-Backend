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

import static com.mapu.infra.oauth.jwt.JwtUtil.AUTHORIZATION;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(AUTHORIZATION)) {
                authorization = cookie.getValue();
            }
        }
        if (authorization == null) {
            //token null
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization;
        if (jwtUtil.isExpired(token)) {
            //token Expired
            filterChain.doFilter(request, response);
            return;
        }

        JwtUserDto jwtUserDto = new JwtUserDto();
        jwtUserDto.setRole(jwtUtil.getName(token));
        jwtUserDto.setRole(jwtUtil.getRole(token));

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(jwtUserDto);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
