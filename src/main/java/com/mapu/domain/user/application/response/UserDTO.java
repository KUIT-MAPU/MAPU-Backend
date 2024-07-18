package com.mapu.domain.user.application.response;

import com.mapu.domain.user.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String email;
    private UserRole role;
    private String nickname;
    private String profile_id;
    private String image;
    private String status;
}
