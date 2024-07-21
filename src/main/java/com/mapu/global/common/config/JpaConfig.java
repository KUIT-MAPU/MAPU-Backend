package com.mapu.global.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.mapu.domain", "com.mapu.infra.health", "com.mapu.infra.oauth"})
@Configuration
public class JpaConfig {
}