package com.mapu.domain.user.domain;

import com.mapu.global.common.domain.BaseEntity;
import com.mapu.infra.oauth.domain.OAuth;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    private Long id;

    @Email
    @NotNull
    @Column(nullable = false, unique = true, length = 20)
    private String email;

    @NotNull
    @Column(nullable = false, length = 10)
    private String role;

    @NotNull
    @Column(nullable = false, length = 20)
    private String nickname;

    @NotNull
    @Column(nullable = false, unique = true, length = 20)
    private String profileId;

    //nullable = true
    @Column(length = 100)
    private String image;

    @NotNull
    @Column(nullable = false, length = 20)
    private String status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private OAuth oauth;

    public static User createUser(String email, String role, String nickname, String profileId, String image, String status) {
        User user = new User();
        user.email = email;
        user.role = role;
        user.nickname = nickname;
        user.profileId = profileId;
        user.image = image;
        user.status = status;
        return user;
    }
}