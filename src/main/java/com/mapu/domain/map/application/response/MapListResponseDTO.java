package com.mapu.domain.map.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MapListResponseDTO {
    private String imageUrl;
    private String title;
    private String region;
    private String description;
    private MapOwnerResponseDTO user;
    private List<String> keyword;
}
