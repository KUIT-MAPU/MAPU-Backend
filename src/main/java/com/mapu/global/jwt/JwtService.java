package com.mapu.global.jwt;

import com.mapu.global.common.exception.BaseException;
import com.mapu.global.common.exception.errorcode.BaseExceptionErrorCode;
import com.mapu.global.jwt.dao.JwtRedisRepository;
import com.mapu.global.jwt.dto.JwtUserDto;
import io.jsonwebtoken.ExpiredJwtException;
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
            throw new BaseException(String.format("%s token is null", tokenType), BaseExceptionErrorCode.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            throw new BaseException(String.format("%s token is expired", tokenType), BaseExceptionErrorCode.BAD_REQUEST);
        }

        // 토큰 type 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(token);
        if (!category.equals(tokenType)) {
            throw new BaseException(String.format("invalid %s token", tokenType), BaseExceptionErrorCode.BAD_REQUEST);
        }
    }

    public JwtUserDto getUserDtoFromToken(String token, String tokenType) {
        checkToken(token, tokenType);
        JwtUserDto jwtUserDto = new JwtUserDto();
        jwtUserDto.setName(jwtUtil.getName(token));
        jwtUserDto.setRole(jwtUtil.getRole(token));
        return jwtUserDto;
    }
}
