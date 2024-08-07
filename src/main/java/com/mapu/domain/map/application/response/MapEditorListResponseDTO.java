package com.mapu.domain.map.application.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MapEditorListResponseDTO {
    List<MapEditorResponseDTO> mapEditors;
    int totalPageCount;

    @Builder
    public MapEditorListResponseDTO(List<MapEditorResponseDTO> mapEditors, int totalPageCount) {
        this.mapEditors = mapEditors;
        this.totalPageCount = totalPageCount;
    }
}
