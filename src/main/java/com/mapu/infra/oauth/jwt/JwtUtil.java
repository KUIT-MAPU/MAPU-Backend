package com.mapu.infra.oauth.jwt;

import com.mapu.infra.oauth.jwt.dto.JwtUserDto;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    public static final String CATEGORY = "category";
    public static final String NAME = "name";
    public static final String ROLE = "role";
    public static final String ACCESS = "access";
    public static final String REFRESH = "refresh";
    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}")String secret) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(CATEGORY, String.class);
    }

    public String getName(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(NAME, String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(ROLE, String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String category, String name, String role, Long expiredMs) {

        return Jwts.builder()
                .claim(CATEGORY, category)
                .claim(NAME, name)
                .claim(ROLE, role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    private Cookie createCookie(String key, String value, int maxAge) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        //cookie.setSecure(true); //TODO https로 전환 후에 주석 해제
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    //TODO expiredMs 상수값 어디서 관리할지 논의 -> 상수, application.yml, ...
    public Cookie createAccessJwtCookie(JwtUserDto jwtUserDto) {
        return createCookie(ACCESS, createJwt(ACCESS, jwtUserDto.getName(), jwtUserDto.getRole(), 60*60*60L), 60*60*60);
    }

    public Cookie createRefreshJwtCookie(JwtUserDto jwtUserDto) {
        return createCookie(REFRESH, createJwt(REFRESH, jwtUserDto.getName(), jwtUserDto.getRole(), 24*60*60L), 24*60*60);
    }

}