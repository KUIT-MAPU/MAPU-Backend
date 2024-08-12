package com.mapu.domain.map.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateMapRequestDTO {

    @NotNull(message = "맵 제목은 필수입니다.")
    private String mapTitle;

    private String mapDescription;

    private String imageUrl;

    @NotNull(message = "주소는 필수입니다.")
    private String address;

    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    @NotNull(message = "줌 레벨은 필수입니다.")
    private Integer zoomLevel;

    private String publishLink;

    @NotNull(message = "검색 노출 여부는 필수입니다.")
    private Boolean isOnSearch;

    private List<String> keywords;

}


