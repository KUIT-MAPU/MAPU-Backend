package com.mapu.infra.oauth.jwt;

import com.mapu.global.common.exception.BaseException;
import com.mapu.global.common.exception.errorcode.BaseExceptionErrorCode;
import com.mapu.global.common.response.BaseResponse;
import com.mapu.infra.oauth.jwt.dto.JwtUserDto;
import com.mapu.infra.oauth.jwt.dto.ReissueResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mapu.infra.oauth.jwt.JwtUtil.REFRESH;

@RequiredArgsConstructor
@RestController
public class JwtReissueController {
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    @PostMapping("/reissue")
    public BaseResponse<ReissueResponseDto> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BaseException(String.format("쿠키에 토큰이 없습니다."), BaseExceptionErrorCode.BAD_REQUEST);
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH)) {
                refresh = cookie.getValue();
            }
        }

        JwtUserDto jwtUserDto = jwtService.getUserDtoFromToken(refresh, REFRESH);
        response.addCookie(jwtUtil.createAccessJwtCookie(jwtUserDto));
        //TODO blacklist old refresh token
        response.addCookie(jwtUtil.createRefreshJwtCookie(jwtUserDto));

        return new BaseResponse<>(new ReissueResponseDto()); //TODO 응답 뭐 넣을지 채우기
    }
}
