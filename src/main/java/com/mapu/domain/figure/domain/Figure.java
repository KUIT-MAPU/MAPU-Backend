package com.mapu.domain.figure.domain;

import com.mapu.domain.map.domain.Map;
import com.mapu.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Figure extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id")
    private Map map;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FigureType type;

    @NotNull
    private String address;

    private int round;

    private int area;

    private int length;

    @NotNull
    private String name;

    @NotNull
    private String text;

    @OneToMany(mappedBy = "figure", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FigureTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "figure", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FigureRate> rate = new ArrayList<>();

    @OneToMany(mappedBy = "figure", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FigureRate> relation = new ArrayList<>();

    @OneToMany(mappedBy = "figure", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FigureRate> vertex = new ArrayList<>();

}
