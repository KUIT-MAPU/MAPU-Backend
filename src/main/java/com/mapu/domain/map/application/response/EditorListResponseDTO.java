package com.mapu.domain.map.application.response;

import com.mapu.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class EditorListResponseDTO {
    private Long userId;
    private String image;
    private String nickname;
    private String profileId;


    public static EditorListResponseDTO from(User user) {
        return EditorListResponseDTO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .image(user.getImage())
                .profileId(user.getProfileId())
                .build();
    }
}

