package com.mapu.global.jwt.api;

import com.mapu.global.jwt.application.JwtService;
import com.mapu.global.jwt.dto.JwtUserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // TODO token null error handling
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];
        JwtUserDto jwtUserDto = jwtService.getUserDtoFromToken(token, ACCESS);
        Authentication authToken = new UsernamePasswordAuthenticationToken(jwtUserDto, null, jwtUserDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}