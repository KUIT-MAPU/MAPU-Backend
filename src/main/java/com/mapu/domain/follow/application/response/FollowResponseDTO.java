package com.mapu.domain.follow.application.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
public class FollowResponseDTO {
    private Long userId;
    private boolean isFollowing;

    @Builder
    public FollowResponseDTO(Long userId, boolean isFollowing) {
        this.userId = userId;
        this.isFollowing = isFollowing;
    }
}
