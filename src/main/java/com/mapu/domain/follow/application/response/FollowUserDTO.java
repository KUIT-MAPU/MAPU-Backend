package com.mapu.domain.follow.application.response;

import lombok.*;

@Getter
@NoArgsConstructor
public class FollowUserDTO {
    private Long userId;
    private String nickname;
    private String profileImageUrl;

    @Builder
    public FollowUserDTO(Long userId, String nickname, String profileImageUrl) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}