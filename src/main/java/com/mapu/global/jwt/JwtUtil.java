package com.mapu.global.jwt;
import com.mapu.global.jwt.dao.JwtRedisRepository;
import com.mapu.global.jwt.domain.JwtRedis;
import com.mapu.global.jwt.dto.JwtUserDto;
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
    private final int accessExpiration;
    private final int refreshExpiration;
    private final JwtRedisRepository jwtRedisRepository;

    public JwtUtil(@Value("${spring.jwt.secret}")String secret,
                   @Value("${spring.jwt.token.access-expiration-time}")String accessExpiration,
                   @Value("${spring.jwt.token.refresh-expiration-time}")String refreshExpiration,
                   JwtRedisRepository jwtRedisRepository) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessExpiration = Integer.parseInt(accessExpiration);
        this.refreshExpiration = Integer.parseInt(refreshExpiration);
        this.jwtRedisRepository = jwtRedisRepository;
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
        Date expiration = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration();
        Date now = new Date(System.currentTimeMillis());
        return expiration.before(now);
    }

    public String createJwt(String category, String name, String role, Long expiredMs) {
        Date expiration = new Date(System.currentTimeMillis() + expiredMs);
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .claim(CATEGORY, category)
                .claim(NAME, name)
                .claim(ROLE, role)
                .issuedAt(now)
                .expiration(expiration)
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

    public Cookie createAccessJwtCookie(JwtUserDto jwtUserDto) {
        String name = jwtUserDto.getName().toString();
        String role = jwtUserDto.getRole().toString();
        String token = createJwt(ACCESS, name, role,accessExpiration*1000L);
        return createCookie(ACCESS, token, accessExpiration);
    }

    public Cookie createRefreshJwtCookie(JwtUserDto jwtUserDto) {
        String name = jwtUserDto.getName().toString();
        String role = jwtUserDto.getRole().toString();
        String token = createJwt(REFRESH, name, role, refreshExpiration*1000L);
        jwtRedisRepository.save(new JwtRedis(name, token, refreshExpiration));
        return createCookie(REFRESH, token, refreshExpiration);
    }
}