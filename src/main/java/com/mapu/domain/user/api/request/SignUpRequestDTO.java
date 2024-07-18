package com.mapu.domain.user.api.request;

import com.mapu.domain.user.domain.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class SignUpRequestDTO {
    @Length(min = 3, max = 12)
    private String nickname;

    @Length(min = 3, max = 20)
    private String profile_id;

    private String image;
}
