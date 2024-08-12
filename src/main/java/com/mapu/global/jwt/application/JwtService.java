package com.mapu.global.jwt.application;

import com.mapu.domain.user.domain.UserRole;
import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.dao.JwtRedisRepository;
import com.mapu.global.jwt.dto.AccessTokenResponseDto;
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

    private void verifyRefreshToken(String token) {
        jwtUtil.checkToken(token, JwtUtil.REFRESH);
        if(!jwtRedisRepository.existsById(token)) {
            throw new JwtException(JwtExceptionErrorCode.UNKNOWN_REFRESH_TOKEN);
        }
    }

    public AccessTokenResponseDto reissueAccessToken(String refresh) {
        verifyRefreshToken(refresh);
        JwtUserDto jwtUserDto = jwtUtil.getUserDtoFromToken(refresh, JwtUtil.REFRESH);
        String accessToken = jwtUtil.createAccessToken(jwtUserDto);

        return new AccessTokenResponseDto(accessToken);
    }

    public void deleteRefreshJwt(String refresh) {
        verifyRefreshToken(refresh);
        jwtRedisRepository.deleteById(refresh);
    }

    public Cookie rotateRefreshToken(String refresh) {
        deleteRefreshJwt(refresh);
        JwtUserDto jwtUserDto = jwtUtil.getUserDtoFromToken(refresh, JwtUtil.REFRESH);
        return jwtUtil.createRefreshJwtCookie(jwtUserDto);
    }

}