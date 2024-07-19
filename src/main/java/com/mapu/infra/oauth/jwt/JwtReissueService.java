package com.mapu.infra.oauth.jwt;

import com.mapu.global.common.exception.BaseException;
import com.mapu.global.common.exception.errorcode.BaseExceptionErrorCode;
import com.mapu.infra.oauth.jwt.dto.JwtUserDto;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mapu.infra.oauth.jwt.JwtUtil.REFRESH;

@RequiredArgsConstructor
@Service
public class JwtReissueService {
    private final JwtUtil jwtUtil;

    private void checkRefreshToken(String refresh) {
        if (refresh == null) {
            //response status code
            throw new BaseException("refresh token null", BaseExceptionErrorCode.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new BaseException("refresh token expired", BaseExceptionErrorCode.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals(REFRESH)) {
            throw new BaseException("invalid refresh token", BaseExceptionErrorCode.BAD_REQUEST);
        }
    }

    public JwtUserDto getUserDtoFromRefreshToken(String refresh) {
        checkRefreshToken(refresh);
        JwtUserDto jwtUserDto = new JwtUserDto();
        jwtUserDto.setName(jwtUtil.getName(refresh));
        jwtUserDto.setRole(jwtUtil.getRole(refresh));
        return jwtUserDto;
    }
}
