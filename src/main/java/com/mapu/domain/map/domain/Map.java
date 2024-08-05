package com.mapu.domain.map.domain;

import com.mapu.domain.user.domain.User;
import com.mapu.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
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

    @OneToMany(mappedBy = "map")
    private List<MapKeyword> keywords;

}
