package com.mapu.domain.home;

import com.mapu.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EditorListResponseDTO {
    private Long userId;
    private String image;
    private String nickname;
    private String profileId;

    @Builder
    public EditorListResponseDTO(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.image = user.getImage();
        this.profileId = user.getProfileId();
    }
}

