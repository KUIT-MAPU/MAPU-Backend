package com.mapu.domain.map.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MapOwnerResponseDTO {
    private String imageUrl;
    private String nickName;
    private String profileId;
}
