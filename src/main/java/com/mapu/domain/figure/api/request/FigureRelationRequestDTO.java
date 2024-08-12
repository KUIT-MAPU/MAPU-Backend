package com.mapu.domain.figure.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FigureRelationRequestDTO {
    private Long relatedFigureId;
}