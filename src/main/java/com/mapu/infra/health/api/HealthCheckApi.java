package com.mapu.infra.health.api;

import com.mapu.global.common.response.BaseResponse;
import com.mapu.infra.health.application.HealthCheckService;
import com.mapu.infra.health.application.response.HealthCheckDTO;
import com.mapu.infra.health.application.response.HealthCheckResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/health-check")
public class HealthCheckApi {
    private final HealthCheckService healthCheckService;

    @GetMapping
    public BaseResponse<HealthCheckResponseDTO> testHealthCheck(){
        HealthCheckResponseDTO response= healthCheckService.getHealthCheckList();
        return new BaseResponse<>(response);
    }
}
