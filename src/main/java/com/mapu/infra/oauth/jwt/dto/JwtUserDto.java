package com.mapu.infra.oauth.jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtUserDto {
    private String role;
    private String name;
}
