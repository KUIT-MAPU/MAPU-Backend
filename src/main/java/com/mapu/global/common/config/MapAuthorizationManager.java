package com.mapu.global.common.config;

import com.mapu.domain.map.application.MapUserRoleService;
import com.mapu.global.jwt.dto.JwtUserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.util.UriTemplate;

import java.util.Map;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class MapAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> { //TODO package 위치 고민

    private final MapUserRoleService mapUserRoleService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();

        // mapId를 URL 경로에서 추출
        String path = request.getRequestURI();
        UriTemplate uriTemplate = new UriTemplate("/map/{mapId}");
        Map<String, String> variables = uriTemplate.match(path);
        long mapId = Long.parseLong(variables.get("mapId"));

        // userId를 Authentication 에서 추출
        Authentication auth = authentication.get();
        JwtUserDto jwtUser = (JwtUserDto) auth.getPrincipal();
        long userId = Long.parseLong(jwtUser.getName());

        boolean access = mapUserRoleService.hasAuthority(mapId, userId);

        //TODO 여기서 권한 없음 예외 처리 직접 해 줘야 할지?
        return new AuthorizationDecision(access);
    }
}