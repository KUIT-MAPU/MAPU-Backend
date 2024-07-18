package com.mapu.infra.oauth.domain;

import com.mapu.domain.user.domain.User;
import com.mapu.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String platform_name;

    @NotNull
    @Column(nullable = false, length = 20)
    private String platform_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public static OAuth createOAuth(String platform_name, String platform_id, User user) {
        OAuth oAuth = new OAuth();
        oAuth.platform_name = platform_name;
        oAuth.platform_id = platform_id;
        oAuth.user = user;
        return oAuth;
    }

}
