package com.mapu.global.jwt;
import com.mapu.domain.user.domain.UserRole;
import com.mapu.global.jwt.dao.JwtRedisRepository;
import com.mapu.global.jwt.domain.JwtRedis;
import com.mapu.global.jwt.dto.JwtUserDto;
import com.mapu.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import com.mapu.global.jwt.exception.JwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
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

    private Claims getPayload(String token) {
        try {
            return Jwts.parser().
                    verifyWith(secretKey).
                    build().
                    parseSignedClaims(token).
                    getPayload();
        } catch (MalformedJwtException e) {
            throw new JwtException(JwtExceptionErrorCode.MALFORMED_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new JwtException(JwtExceptionErrorCode.EXPIRED_JWT_TOKEN);
        } catch (Exception e) {
            throw new JwtException(JwtExceptionErrorCode.INVALID_JWT_TOKEN);
        }
    }

    public String getCategory(String token) {
        return getPayload(token).get(CATEGORY, String.class);
    }

    public String getName(String token) {

        return getPayload(token).get(NAME, String.class);
    }

    public String getRole(String token) {

        return getPayload(token).get(ROLE, String.class);
    }

    public Boolean isExpired(String token) {
        Date expiration = getPayload(token).getExpiration();
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

    public String createAccessToken(JwtUserDto jwtUserDto){
        String name = jwtUserDto.getName();
        String role = jwtUserDto.getRole().toString();
        return createJwt(ACCESS, name, role,accessExpiration*1000L);
    }

    public String createRefreshToken(JwtUserDto jwtUserDto) {
        String name = jwtUserDto.getName();
        String role = jwtUserDto.getRole().toString();
        return createJwt(REFRESH, name, role, refreshExpiration*1000L);
    }

    public Cookie createRefreshJwtCookie(JwtUserDto jwtUserDto) {
        String token = createRefreshToken(jwtUserDto);
        jwtRedisRepository.save(new JwtRedis(token, refreshExpiration));
        return createCookie(REFRESH, token, refreshExpiration);
    }


    public void checkToken(String token, String tokenType) {
        if (token == null) {
            JwtExceptionErrorCode errorCode = JwtExceptionErrorCode.NO_JWT_TOKEN;
            errorCode.addTokenTypeInfoToMessage(tokenType);
            throw new JwtException(errorCode);
        }

        //expired check
        try {
            isExpired(token);
        } catch (ExpiredJwtException e) {
            JwtExceptionErrorCode errorCode = JwtExceptionErrorCode.EXPIRED_JWT_TOKEN;
            errorCode.addTokenTypeInfoToMessage(tokenType);
            throw new JwtException(errorCode);
        }

        // 토큰 type 확인 (발급시 페이로드에 명시)
        String category = getCategory(token);
        if (!category.equals(tokenType)) {
            JwtExceptionErrorCode errorCode = JwtExceptionErrorCode.WRONG_JWT_TOKEN_TYPE;
            errorCode.addTokenTypeInfoToMessage(tokenType);
            throw new JwtException(errorCode);
        }
    }


    public JwtUserDto getUserDtoFromToken(String token, String tokenType) {
        checkToken(token, tokenType);
        return JwtUserDto.builder().name(Long.valueOf(getName(token)))
                .role(UserRole.valueOf(getRole(token)))
                .build();
    }
}