package com.mapu.global.jwt.filter;
import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.application.JwtService;
import com.mapu.global.jwt.dto.JwtUserDto;
import com.mapu.global.jwt.exception.JwtException;
import com.mapu.global.jwt.exception.errorcode.JwtExceptionErrorCode;
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
import java.util.List;

import static com.mapu.global.jwt.JwtUtil.ACCESS;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    //전체 허용된 URL 패턴 리스트
    private final List<String> PERMIT_ALL_URL_LIST = List.of(
            "/user/signin",
            "/user/signup",
            "/map",
            "/search/map",
            "/jwt/reissue",
            "/error",
            "/home/keyword"
    );
    //익명사용자 구분 필요 URL 패턴 리스트
    private final List<String> ANONYMOUS_URL_LIST = List.of(
            "/home",
            "/user/maps",
            "/home/editor"
    );
    //GET 메서드만 허용 URL 패턴 리스트
    private final List<String> ONLY_GET_ANONYMOUS_URL_LIST = List.of(
            "/user"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String authorization = request.getHeader("Authorization");

        if(isUrlPermitted(PERMIT_ALL_URL_LIST,requestUri)) {
            filterChain.doFilter(request,response);
            return;
        }
        if(isUrlPermitted(ANONYMOUS_URL_LIST,requestUri)){
            if(authorization==null){
                //익명 사용자
                filterChain.doFilter(request,response);
                return;
            }
        }
        if(isUrlPermitted(ONLY_GET_ANONYMOUS_URL_LIST,requestUri)){
             if(authorization==null && request.getMethod().equals("GET")){
                 filterChain.doFilter(request,response);
                 return;
             }
        }

        if (authorization == null) {
            throw new JwtException(JwtExceptionErrorCode.NO_JWT_TOKEN);
        }
        if (!authorization.startsWith("Bearer ")) {
            throw new JwtException(JwtExceptionErrorCode.NO_BEARER_TYPE);
        }
        String token = authorization.split(" ")[1];
        //TODO: 로그아웃 블랙리스트 관리 및 예외처리 필요
        //유효한 토큰인지 검증
        jwtUtil.checkToken(token, ACCESS);

        JwtUserDto jwtUserDto = jwtUtil.getUserDtoFromToken(token, ACCESS);
        Authentication authToken = new UsernamePasswordAuthenticationToken(jwtUserDto, null, jwtUserDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }


    private boolean matchesPattern(String pattern,String requestUri) {
        // **을 포함한 패턴을 정규식으로 변환
        String regex = pattern.replace("**", ".*");
        return requestUri.matches(regex);
    }

    public boolean isUrlPermitted(List<String> urlList,String requestUri) {
        for (String pattern : urlList) {
            if (matchesPattern(pattern, requestUri)) {
                return true;
            }
        }
        return false;
    }

}