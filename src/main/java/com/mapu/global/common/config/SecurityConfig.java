package com.mapu.global.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapu.global.common.response.BaseResponse;
import com.mapu.global.jwt.JwtFilter;
import com.mapu.global.jwt.JwtLogoutHandler;
import com.mapu.global.jwt.application.JwtService;
import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.dao.JwtRedisRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final JwtRedisRepository jwtRedisRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        http
                .logout((logout) ->
                        logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                                .deleteCookies(jwtUtil.REFRESH)
                                .addLogoutHandler(new JwtLogoutHandler(jwtService))
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    response.setStatus(HttpServletResponse.SC_OK);
                                    response.setContentType("application/json;charset=UTF-8");
                                    BaseResponse<String> baseResponse = new BaseResponse<>("로그아웃 성공");
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    String jsonResponse = objectMapper.writeValueAsString(baseResponse);
                                    response.getWriter().write(jsonResponse);
                                }));

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //JWTFilter 추가
        http
                .addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
                //.addFilterBefore(new CustomLogoutFilter(jwtUtil, jwtRedisRepository), LogoutFilter.class);

        //oauth2 -> 필요없음
//        http
//                .oauth2Login(Customizer.withDefaults());

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/user/signin/**", "/user/signup/**").permitAll()
                        .requestMatchers("/jwt/reissue").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated());

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}