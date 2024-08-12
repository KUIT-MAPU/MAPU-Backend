package com.mapu.global.common.config;

import com.mapu.domain.map.application.MapUserRoleService;
import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.filter.JwtExceptionFilter;
import com.mapu.global.jwt.filter.JwtFilter;
import com.mapu.global.jwt.filter.JwtLogoutFilter;
import com.mapu.global.jwt.application.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final MapUserRoleService mapUserRoleService;


    private final String[] WHITE_LIST = new String[]{
            "/user/signin/**",
            "/user/signup/**",
            "/map",
            "/search/map",
            "/jwt/reissue",
            "/error",
            "/home/editor",
            "/home/keyword",
            "/map/logined",
            "/map/list/**",
            "/map/search"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(HttpMethod.GET,"/user").permitAll()
                        .requestMatchers("/map/bookmark", "/map/create").authenticated()
                        .requestMatchers("/map/**").access(new MapAuthorizationManager(mapUserRoleService))
                        .anyRequest().authenticated());

        //JWTFilter 추가
        http
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new JwtLogoutFilter(jwtService), LogoutFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtFilter.class );

        //CORS 설정
        http
                .cors((cors)-> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(List.of(("https://mapu-frontend.vercel.app"),
                                ("http://localhost:9000"),("http://localhost:3000")));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}