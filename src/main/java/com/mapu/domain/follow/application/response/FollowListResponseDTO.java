package com.mapu.domain.follow.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class FollowListResponseDTO {
    private Long userId;
    private List<FollowUserDTO> users;

    @Builder
    public FollowListResponseDTO(Long userId, List<FollowUserDTO> users) {
        this.userId = userId;
        this.users = users;
    }
}