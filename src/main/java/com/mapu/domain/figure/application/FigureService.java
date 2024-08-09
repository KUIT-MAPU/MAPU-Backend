package com.mapu.domain.figure.application;

import com.mapu.domain.figure.application.response.FigureResponseDTO;
import com.mapu.domain.figure.dao.FigureRepository;
import com.mapu.domain.figure.domain.Figure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FigureService {

    private final FigureRepository figureRepository;

    public FigureResponseDTO getFigure(Long figureId) {
        Figure figure = figureRepository.findById(figureId).orElseThrow(() -> new RuntimeException());
        return FigureResponseDTO.from(figure);
    }

}
