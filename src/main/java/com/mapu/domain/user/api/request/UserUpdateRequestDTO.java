package com.mapu.domain.user.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDTO {
    @NotNull(message = "닉네임은 필수입니다.")
    @Length(min = 3, max = 12,
            message = "최소 {min}자리 ~ 최대 {max}자리까지 가능합니다")
    private String nickname;

    @NotBlank(message = "프로필 아이디는 필수입니다.")
    @Length(min = 3, max = 20,
            message = "최소 {min}자리 ~ 최대 {max}자리까지 가능합니다")
    @Pattern(regexp = "^[a-z0-9._]+$", message = "영어 소문자, 숫자, 특수문자(.,_)만 사용가능합니다.")
    private String profileId;
}
