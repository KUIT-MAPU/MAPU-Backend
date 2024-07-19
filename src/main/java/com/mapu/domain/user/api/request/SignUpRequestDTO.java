package com.mapu.domain.user.api.request;

import com.mapu.domain.user.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDTO {
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 3, max = 12, message = "닉네임은 3글자 이상, 12글자 이하여야 합니다.")
    private String nickname;

    @NotBlank(message = "프로필 아이디는 필수입니다.")
    @Size(min = 3, max = 20, message = "프로필 아이디는 3글자 이상, 20글자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z0-9._]+$", message = "프로필 아이디는 영어 소문자, 숫자, 특수문자(.,_)만 사용가능합니다.")
    private String profileId;

}
