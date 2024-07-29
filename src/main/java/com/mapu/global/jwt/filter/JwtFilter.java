package com.mapu.global.jwt.filter;

import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.application.JwtService;
import com.mapu.global.jwt.dto.JwtUserDto;
import com.mapu.global.jwt.exception.JwtException;
import com.mapu.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.mapu.global.jwt.JwtUtil.ACCESS;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final JwtUtil jwtUtil;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 허용된 URL 패턴 리스트
    private final List<String> permitAllUrls = List.of(
            "/user/signin/**",
            "/user/signup",
            "/jwt/reissue",
            "/error"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String authorization = request.getHeader("Authorization");

        // 허용된 URL 패턴인지 확인
        if (isPermitAllUrl(requestUri)) {
            filterChain.doFilter(request,response);
            return;
        }

        String token = authorization.split(" ")[1];

        JwtUserDto jwtUserDto = jwtService.getUserDtoFromToken(token, ACCESS);
        Authentication authToken = new UsernamePasswordAuthenticationToken(jwtUserDto, null, jwtUserDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    private boolean isPermitAllUrl(String requestUri) {
        return permitAllUrls.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
    }

    private void checkCategory(String token) {
        String category = jwtUtil.getCategory(token);
        if (!category.equals("access")) {
            JwtExceptionErrorCode errorCode = JwtExceptionErrorCode.INVALID_JWT_TOKEN;
            errorCode.addTokenTypeInfoToMessage("access");
            throw new JwtException(errorCode);
        }
    }

    private void checkExpired(String token) {
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            JwtExceptionErrorCode errorCode = JwtExceptionErrorCode.EXPIRED_JWT_TOKEN;
            errorCode.addTokenTypeInfoToMessage("access");
            throw new JwtException(errorCode);
        }
    }

    private void checkHeader(String authorization) {
        if (authorization == null) {
            throw new JwtException(JwtExceptionErrorCode.NO_JWT_TOKEN_IN_HEADER);
        } else if (!authorization.startsWith("Bearer ")) {
            throw new JwtException(JwtExceptionErrorCode.NO_BEARER_TYPE);
        }
    }

}