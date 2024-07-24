package com.mapu.global.jwt.domain;

import com.mapu.global.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken")
public class JwtRedis extends BaseEntity {
    @Id
    private String tokenValue;

    @TimeToLive
    private int expiration;
}