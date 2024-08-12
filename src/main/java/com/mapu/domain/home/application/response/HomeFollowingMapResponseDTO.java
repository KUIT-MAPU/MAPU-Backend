package com.mapu.domain.home.application.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Builder
@Getter
public class HomeFollowingMapResponseDTO {
    private String nickname;
    private String profileId;
    private String userImage;
    private List<MapDTO> maps;

    @Builder
    @Getter
    public static class MapDTO {
        private String title;
        private String address;
        private String imageUrl;
    }
}
