package com.mapu.domain.figure.api;

import com.mapu.domain.figure.api.request.FigureRateRequestDTO;
import com.mapu.domain.figure.api.request.FigureRelationRequestDTO;
import com.mapu.domain.figure.api.request.FigureTagRequestDTO;
import com.mapu.domain.figure.application.FigureService;
import com.mapu.domain.figure.application.response.FigureResponseDTO;
import com.mapu.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/figure")
@RequiredArgsConstructor
public class FigureController {

    private final FigureService figureService;

    //객체 정보 조회
    @GetMapping("/{figureId}")
    public BaseResponse<FigureResponseDTO> getFigure(@PathVariable Long figureId) {
        System.out.println(figureId);
        FigureResponseDTO figureDTO = figureService.getFigure(figureId);
        System.out.println( figureService.getFigure(figureId));
        return new BaseResponse<>(figureDTO);
    }

    /**
     * 객체 연결 관련 API
     */

    //객체 연결 추가
    @PostMapping("/{figureId}/relation")
    public BaseResponse<Long> addRelation(@PathVariable Long figureId, @RequestBody FigureRelationRequestDTO request) {
        Long relationId = figureService.addRelation(figureId, request.getRelatedFigureId());
        return new BaseResponse<>(relationId);
    }

    @DeleteMapping("/{figureId}/relation")
    public BaseResponse<Void> removeRelation(@PathVariable Long figureId, @RequestParam Long relatedFigureId) {
        figureService.removeRelation(figureId, relatedFigureId);
        return new BaseResponse<>(null);
    }

    // 모든 연결 삭제
    @DeleteMapping("/{figureId}/relations")
    public BaseResponse<Void> removeAllRelations(@PathVariable Long figureId) {
        figureService.removeAllRelations(figureId);
        return new BaseResponse<>(null);
    }

    /**
     * 태그 관련 API
     */

    // 태그 추가
    @PostMapping("/{figureId}/tag")
    public BaseResponse<Long> addTag(@PathVariable Long figureId, @RequestBody FigureTagRequestDTO request) {
        Long tagId = figureService.addTag(figureId, request.getTag());
        return new BaseResponse<>(tagId);
    }

    @DeleteMapping("/{figureId}/tag/{tagId}")
    public BaseResponse<Void> removeTag(@PathVariable Long figureId, @PathVariable Long tagId) {
        figureService.removeTag(figureId, tagId);
        return new BaseResponse<>(null);
    }


    // 태그 모두 삭제
    @DeleteMapping("/{figureId}/tags")
    public BaseResponse<Void> removeAllTags(@PathVariable Long figureId) {
        figureService.removeAllTags(figureId);
        return new BaseResponse<>(null);
    }


    /**
     *별점 관련 API
     * 반환값 rateId 추가되어야함
     */
    @PostMapping("/{figureId}/rate")
    public BaseResponse<Long> addRate(@PathVariable Long figureId, @RequestBody FigureRateRequestDTO request) {
        Long rateId = figureService.addRate(figureId, request.getRateName(), request.getRateStar());
        return new BaseResponse<>(rateId);
    }

    @DeleteMapping("/{figureId}/rate/{rateId}")
    public BaseResponse<Void> removeRate(@PathVariable Long figureId, @PathVariable Long rateId) {
        figureService.removeRate(figureId, rateId);
        return new BaseResponse<>(null);
    }

    @DeleteMapping("/{figureId}/rates")
    public BaseResponse<Void> removeAllRates(@PathVariable Long figureId) {
        figureService.removeAllRates(figureId);
        return new BaseResponse<>(null);
    }


}
