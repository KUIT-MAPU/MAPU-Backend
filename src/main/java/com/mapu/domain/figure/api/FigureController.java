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

    private final FigureService figureService;

    //객체 기본정보 조회ㅣ
    @GetMapping("/{figureId}")
    public BaseResponse<FigureResponseDTO> getFigure(@PathVariable Long figureId) {
        System.out.println(figureId);
        FigureResponseDTO figureDTO = figureService.getFigure(figureId);
        System.out.println( figureService.getFigure(figureId));
        return new BaseResponse<>(figureDTO);
    }

    //객체 정보 속성
    @GetMapping("/{figureId}/detail")
    public BaseResponse<Void> getFigureDetail(@PathVariable Long figureId) {
    return null;
    }


}
