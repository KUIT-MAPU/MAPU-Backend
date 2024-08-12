package com.mapu.domain.follow.application.response;

import lombok.*;

import java.util.List;

@Getter
public class FollowListResponseDTO {
    private Long userId;
    private List<FollowUserDTO> users;

    @Builder
    public FollowListResponseDTO(Long userId, List<FollowUserDTO> users) {
        this.userId = userId;
        this.users = users;
    }
}