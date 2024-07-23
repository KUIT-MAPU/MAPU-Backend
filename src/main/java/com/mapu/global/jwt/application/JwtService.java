package com.mapu.global.jwt.application;

import com.mapu.domain.user.domain.UserRole;
import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.dao.JwtRedisRepository;
import com.mapu.global.jwt.dto.JwtUserDto;
import com.mapu.global.jwt.exception.JwtException;
import com.mapu.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtUtil jwtUtil;
    private final JwtRedisRepository jwtRedisRepository;

    private void checkToken(String token, String tokenType) {
        if (token == null) {
            //response status code
            JwtExceptionErrorCode errorCode = JwtExceptionErrorCode.NO_JWT_TOKEN_IN_COOKIE;
            errorCode.addTokenTypeInfoToMessage(tokenType);
            throw new JwtException(errorCode);
        }

        //expired check
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            JwtExceptionErrorCode errorCode = JwtExceptionErrorCode.EXPIRED_JWT_TOKEN;
            errorCode.addTokenTypeInfoToMessage(tokenType);
            throw new JwtException(errorCode);
        }

        // 토큰 type 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(token);
        if (!category.equals(tokenType)) {
            JwtExceptionErrorCode errorCode = JwtExceptionErrorCode.INVALID_JWT_TOKEN;
            errorCode.addTokenTypeInfoToMessage(tokenType);
            throw new JwtException(errorCode);
        }
    }

    public JwtUserDto getUserDtoFromToken(String token, String tokenType) {
        checkToken(token, tokenType);
        JwtUserDto jwtUserDto = JwtUserDto.builder().name(Long.valueOf(jwtUtil.getName(token)))
                .role(UserRole.valueOf(jwtUtil.getRole(token)))
                .build();
        return jwtUserDto;
    }

    private void verifyRefreshToken(String token) {
        checkToken(token, jwtUtil.REFRESH);
        if(!jwtRedisRepository.existsById(token)) {
            throw new JwtException(JwtExceptionErrorCode.UNKNOWN_REFRESH_TOKEN);
        }
    }

    public AccessTokenResponseDto reissueAccessToken(String refresh) {
        verifyRefreshToken(refresh);
        JwtUserDto jwtUserDto = getUserDtoFromToken(refresh, JwtUtil.REFRESH);
        String accessToken = jwtUtil.createAccessToken(jwtUserDto);

        return new AccessTokenResponseDto(accessToken);
    }

    public Cookie rotateRefreshToken(String refresh) {
        verifyRefreshToken(refresh);
        JwtUserDto jwtUserDto = getUserDtoFromToken(refresh, JwtUtil.REFRESH);
        return jwtUtil.rotateRefreshJwtCookie(jwtUserDto, refresh);
    }


    public void deleteRefreshJwt(String refresh) {
        verifyRefreshToken(refresh);
        jwtRedisRepository.deleteById(refresh);
    }
}