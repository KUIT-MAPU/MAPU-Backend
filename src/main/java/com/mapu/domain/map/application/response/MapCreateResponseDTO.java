package com.mapu.domain.map.application.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MapCreateResponseDTO {

    private Long mapId;
    @Builder
    public MapCreateResponseDTO(Long mapId) {
        this.mapId = mapId;
    }
}
