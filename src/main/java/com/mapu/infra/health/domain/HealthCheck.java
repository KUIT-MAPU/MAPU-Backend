package com.mapu.infra.health.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class HealthCheck {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="healthcheck_id")
    private Long id;

    private String name;
    private Long age;
}
