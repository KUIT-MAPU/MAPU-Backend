package com.mapu.domain.map.domain;

import com.mapu.domain.figure.domain.Figure;
import com.mapu.domain.figure.domain.FigureTag;
import com.mapu.domain.user.domain.User;
import com.mapu.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Map extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String mapTitle;

    private String mapDescription;

    @NotNull
    private String imageUrl;

    @NotNull
    private String address;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    private int zoomLevel;

    private String publishLink;

    @NotNull
    private boolean isOnSearch;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MapKeyword> keywords;

    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Figure> figure = new ArrayList<>();

    @Builder
    public Map(String mapTitle, String mapDescription, String address, double latitude, double longitude,
               int zoomLevel, String publishLink, boolean isOnSearch, User user,String imageUrl) {
        this.mapTitle = mapTitle;
        this.mapDescription = mapDescription;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoomLevel = zoomLevel;
        this.publishLink = publishLink;
        this.isOnSearch = isOnSearch;
        this.user = user;
        this.imageUrl = imageUrl;
    }
}
