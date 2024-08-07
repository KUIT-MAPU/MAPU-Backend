package com.mapu.domain.figure.application;

import com.mapu.domain.figure.application.response.FigureResponseDTO;
import com.mapu.domain.figure.dao.FigureRepository;
import com.mapu.domain.figure.domain.Figure;

public class FigureService {

    private FigureRepository figureRepository;

    public FigureResponseDTO getFigure(Long figureId) {
        Figure figure = figureRepository.findById(figureId).orElseThrow(() -> new RuntimeException());
        return FigureResponseDTO.from(figure);
    }

}
