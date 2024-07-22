package com.mapu.global.jwt.application;

import com.mapu.global.common.exception.BaseException;
import com.mapu.global.common.exception.errorcode.BaseExceptionErrorCode;
import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.dao.JwtRedisRepository;
import com.mapu.global.jwt.dto.JwtUserDto;
import com.mapu.global.jwt.exception.JwtException;
import com.mapu.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtUtil jwtUtil;

    private void checkToken(String token, String tokenType) {
        if (token == null) {
            //response status code
            JwtExceptionErrorCode errorCode = JwtExceptionErrorCode.NO_JWT_TOKEN_IN_COOKIE;
            errorCode.setMessage(String.format("%s token is null", tokenType));
            throw new JwtException(errorCode);
        }

        //expired check
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            JwtExceptionErrorCode errorCode = JwtExceptionErrorCode.EXPIRED_JWT_TOKEN;
            errorCode.setMessage(String.format("%s token is expired", tokenType));
            throw new JwtException(errorCode);
        }

        // 토큰 type 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(token);
        if (!category.equals(tokenType)) {
            JwtExceptionErrorCode errorCode = JwtExceptionErrorCode.INVALID_JWT_TOKEN;
            errorCode.setMessage(String.format("invalid %s token", tokenType));
            throw new JwtException(errorCode);
        }
    }

    public JwtUserDto getUserDtoFromToken(String token, String tokenType) {
        checkToken(token, tokenType);
        JwtUserDto jwtUserDto = JwtUserDto.builder().name(jwtUtil.getName(token))
                .role(jwtUtil.getRole(token))
                .build();
        return jwtUserDto;
    }
}