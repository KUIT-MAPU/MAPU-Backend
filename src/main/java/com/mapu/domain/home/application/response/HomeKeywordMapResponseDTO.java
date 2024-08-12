package com.mapu.domain.home.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class HomeKeywordMapResponseDTO {
    private String keyword;
    private List<KeywordMapDTO> maps;

    @Builder
    @Getter
    public static class KeywordMapDTO {
        private String nickname;
        private String profileId;
        private String userImage;
        private String mapTitle;
        private String mapImage;
    }
}