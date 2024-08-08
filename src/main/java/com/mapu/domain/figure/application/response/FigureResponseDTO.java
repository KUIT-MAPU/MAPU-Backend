package com.mapu.domain.figure.application.response;

import com.mapu.domain.figure.domain.Figure;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FigureResponseDTO {
    private Long id;
    private String type;
    private String address;
    private int round;
    private int area;
    private int length;
    private String name;
    private String text;

    public static FigureResponseDTO from(Figure figure) {
        return FigureResponseDTO.builder()
                .id(figure.getId())
                .type(String.valueOf(figure.getType()))
                .address(figure.getAddress())
                .round(figure.getRound())
                .area(figure.getArea())
                .length(figure.getLength())
                .name(figure.getName())
                .text(figure.getText())
                .build();
    }
}
