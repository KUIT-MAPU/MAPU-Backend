package com.mapu.infra.oauth.domain;

import com.mapu.domain.user.domain.User;
import com.mapu.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class OAuth  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    private Long id;

    @NotNull
    @Column(nullable = false, length = 10)
    private String platformName;

    @NotNull
    @Column(nullable = false, length = 20)
    private String platformId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public OAuth(String platformName, String platformId, User user) {
        this.platformName = platformName;
        this.platformId = platformId;
        this.user = user;
    }
}
