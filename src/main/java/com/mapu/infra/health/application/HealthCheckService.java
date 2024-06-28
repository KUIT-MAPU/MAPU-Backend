package com.mapu.infra.health.application;

import com.mapu.infra.health.application.response.HealthCheckResponseDTO;
import com.mapu.infra.health.dao.HealthCheckRepository;
import com.mapu.infra.health.domain.HealthCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthCheckService {
    private final HealthCheckRepository healthCheckRepository;
    public HealthCheckResponseDTO getHealthCheckList() {
        List<HealthCheck> healthChecks = healthCheckRepository.findAll();

        return HealthCheckResponseDTO.builder()
                .healthChecks(healthChecks)
                .build();
    }
}
