package com.mapu.infra.health.application.response;

import com.mapu.infra.health.domain.HealthCheck;
import lombok.Builder;
import lombok.Getter;

@Getter
public class HealthCheckDTO {
    Long id;
    String name;
    Long age;

    @Builder
    public HealthCheckDTO(HealthCheck healthCheck) {
        this.id = healthCheck.getId();
        this.name = healthCheck.getName();
        this.age = healthCheck.getAge();
    }
}
