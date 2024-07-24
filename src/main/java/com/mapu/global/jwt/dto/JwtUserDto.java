package com.mapu.global.jwt.dto;

import com.mapu.domain.user.domain.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class JwtUserDto implements AuthenticatedPrincipal {
    private UserRole role;
    private Long name;

    public Map<String, Object> getAttributes() {
        return null;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> role.toString());
        return collection;
    }

    @Builder
    public JwtUserDto(UserRole role, Long name) {
        this.role = role;
        this.name = name;
    }

    @Override
    public String getName() {
        return String.valueOf(this.name);
    }
}