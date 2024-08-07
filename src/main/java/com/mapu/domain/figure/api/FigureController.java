package com.mapu.domain.figure.api;

import com.mapu.domain.figure.application.FigureService;
import com.mapu.domain.figure.application.response.FigureResponseDTO;
import com.mapu.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/figure")
@RequiredArgsConstructor
public class FigureController {

    private FigureService figureService;

    @GetMapping("/{figureId}")
    public BaseResponse<FigureResponseDTO> getFigure(@PathVariable Long figureId) {
        FigureResponseDTO figureDTO = figureService.getFigure(figureId);
        return new BaseResponse<>(figureDTO);
    }


}
