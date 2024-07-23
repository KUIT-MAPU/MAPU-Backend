package com.mapu.domain.user.domain;

import com.mapu.global.common.domain.BaseEntity;
import com.mapu.infra.oauth.domain.OAuth;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
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

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotNull(message = "이메일은 필수입니다.")
    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @NotNull(message = "역할은 필수입니다.")
    @Column(nullable = false, length = 10)
    private String role;

    @NotNull(message = "닉네임은 필수입니다.")
    @Size(min = 3, max = 12, message = "닉네임은 3글자 이상, 12글자 이하여야 합니다.")
    @Column(nullable = false, length = 20)
    private String nickname;

    @NotNull(message = "프로필 아이디는 필수입니다.")
    @Size(min = 3, max = 20, message = "프로필 아이디는 3글자 이상, 20글자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z0-9._]+$", message = "프로필 아이디는 영어 소문자, 숫자, 특수문자(.,_)만 사용가능합니다.")
    @Column(nullable = false, unique = true, length = 20)
    private String profileId;

    //nullable = true
    @Column(length = 500)
    private String image;

    @NotNull(message = "상태는 필수입니다.")
    @Column(nullable = false, length = 20)
    private String status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private OAuth oauth;

    @Builder
    public User(String email, String role, String nickname, String profileId, String image, String status) {
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.profileId = profileId;
        this.image = image;
        this.status = status;
    }
}