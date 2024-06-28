package com.mapu.infra.health.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application.yml")
@DataJpaTest
class HealthCheckRepositoryTest {
    @Autowired
    HealthCheckRepository healthCheckRepository;

    @DisplayName("DB 초기화 후, 전체 행 개수를 조회하면 4이어야 한다.")
    @Test
    void countAllResult4AfterInit(){
        //when
        long count = healthCheckRepository.count();
        //then
        assertThat(count).isEqualTo(4);
    }
}