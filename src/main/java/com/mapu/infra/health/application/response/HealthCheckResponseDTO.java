package com.mapu.infra.health.application.response;

import com.mapu.infra.health.domain.HealthCheck;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class HealthCheckResponseDTO {
    List<HealthCheck> healthChecks;

    @Builder
    public HealthCheckResponseDTO(List<HealthCheck> healthChecks) {
        this.healthChecks = healthChecks;
    }
}
