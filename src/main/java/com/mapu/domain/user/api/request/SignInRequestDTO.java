package com.mapu.domain.user.api.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignInRequestDTO {

    @NotNull(message = "kakao와 google 중 소셜로그인 타입을 명시해야합니다.")
    private String socialType;

    @NotNull(message = "social login에 필요한 code가 필요합니다.")
    private String code;
}
