package com.mapu.global.jwt.dao;

import com.mapu.global.jwt.domain.JwtRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRedisRepository extends CrudRepository<JwtRedis, String> {
    Optional<JwtRedis> findByRefreshToken(String refreshToken);
}
