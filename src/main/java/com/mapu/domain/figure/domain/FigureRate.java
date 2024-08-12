package com.mapu.domain.figure.domain;

import com.mapu.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class FigureRate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "figure_id")
    private Figure figure;

    @NotNull
    @Column(name = "rate_name")
    private String rateName;

    @NotNull
    @Column(name = "rate_star")
    private int rateStar;

    @Builder
    public FigureRate(Figure figure, String rate_name, int rate_star) {
        this.figure = figure;
        this.rateName = rate_name;
        this.rateStar = rate_star;
    }

}
