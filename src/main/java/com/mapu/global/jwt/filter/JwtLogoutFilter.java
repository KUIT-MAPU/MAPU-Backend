package com.mapu.global.jwt.filter;

import com.mapu.global.jwt.JwtUtil;
import com.mapu.global.jwt.application.JwtService;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JwtLogoutFilter extends LogoutFilter {

    public JwtLogoutFilter(JwtService jwtService) {
        super(new JwtLogoutSuccessHandler(),
                new JwtLogoutHandler(jwtService), new CookieClearingLogoutHandler(JwtUtil.REFRESH));
        super.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout", HttpMethod.POST.name()));
    }
}
