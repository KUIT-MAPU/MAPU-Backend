package com.mapu.domain.figure.application;

import com.mapu.domain.figure.application.response.FigureResponseDTO;
import com.mapu.domain.figure.dao.FigureRateRepository;
import com.mapu.domain.figure.dao.FigureRelationRepository;
import com.mapu.domain.figure.dao.FigureRepository;
import com.mapu.domain.figure.dao.FigureTagRepository;
import com.mapu.domain.figure.domain.Figure;
import com.mapu.domain.figure.domain.FigureRate;
import com.mapu.domain.figure.domain.FigureRelation;
import com.mapu.domain.figure.domain.FigureTag;
import com.mapu.domain.figure.exception.FigureException;
import com.mapu.domain.figure.exception.errorcode.FigureExceptionErrorCode;
import jakarta.persistence.EntityNotFoundException;
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
    private final FigureRelationRepository figureRelationRepository;
    private final FigureTagRepository figureTagRepository;
    private final FigureRateRepository figureRateRepository;

    public FigureResponseDTO getFigure(Long figureId) {
        Figure figure = findFigureById(figureId);
        return FigureResponseDTO.from(figure);
    }

    // 객체 연결 관계 추가
    public Long addRelation(Long figureId, Long relatedFigureId) {
        try {
            Figure figure = findFigureById(figureId);
            Figure relatedFigure = findFigureById(relatedFigureId);

            FigureRelation relation = new FigureRelation(figure, relatedFigure);
            relation = figureRelationRepository.save(relation);
            return relation.getId();
        } catch (EntityNotFoundException e) {
            throw new FigureException(FigureExceptionErrorCode.FIGURE_NOT_FOUND);
        } catch (FigureException e) {
            throw e; // 이미 존재하는 관계에 대한 예외 처리
        } catch (Exception e) {
            throw new FigureException(FigureExceptionErrorCode.FIGURE_OPERATION_FAILED);
        }
    }


    // 객체 연결 삭제
    public void removeRelation(Long figureId, Long relatedFigureId) {
        try {
            figureRelationRepository.deleteByFigureIdAndFigure2Id(figureId, relatedFigureId);
        } catch (Exception e) {
            throw new FigureException(FigureExceptionErrorCode.FIGURE_RELATION_NOT_FOUND);
        }
    }
    // 모든 연결 삭제
    public void removeAllRelations(Long figureId) {
        figureRelationRepository.deleteAllByFigureId(figureId);
    }

    // 태그 추가
    public Long addTag(Long figureId, String tag) {
        try {
            Figure figure = findFigureById(figureId);
            FigureTag figureTag = new FigureTag(figure, tag);
            figureTag = figureTagRepository.save(figureTag);
            return figureTag.getId();
        } catch (EntityNotFoundException e) {
            throw new FigureException(FigureExceptionErrorCode.FIGURE_NOT_FOUND);
        } catch (Exception e) {
            throw new FigureException(FigureExceptionErrorCode.FIGURE_OPERATION_FAILED);
        }
    }

    public void removeTag(Long figureId, Long tagId) {
        try {
            FigureTag tag = figureTagRepository.findByIdAndFigureId(tagId, figureId)
                    .orElseThrow(() -> new FigureException(FigureExceptionErrorCode.FIGURE_TAG_NOT_FOUND));
            figureTagRepository.delete(tag);
        } catch (Exception e) {
            throw new FigureException(FigureExceptionErrorCode.FIGURE_OPERATION_FAILED);
        }
    }

    // 모든 태그 삭제
    public void removeAllTags(Long figureId) {
        figureTagRepository.deleteAllByFigureId(figureId);
    }

    // 별점 추가
    public Long addRate(Long figureId, String rateName, int rateStar) {
        try {
            Figure figure = findFigureById(figureId);
            FigureRate figureRate = new FigureRate(figure, rateName, rateStar);
            figureRate = figureRateRepository.save(figureRate);
            return figureRate.getId();
        } catch (EntityNotFoundException e) {
            throw new FigureException(FigureExceptionErrorCode.FIGURE_NOT_FOUND);
        } catch (Exception e) {
            throw new FigureException(FigureExceptionErrorCode.FIGURE_OPERATION_FAILED);
        }
    }

    // 별점 하나 제거
    public void removeRate(Long figureId, Long rateId) {
        try {
            FigureRate rate = figureRateRepository.findByIdAndFigureId(rateId, figureId)
                    .orElseThrow(() -> new FigureException(FigureExceptionErrorCode.FIGURE_RATE_NOT_FOUND));
            figureRateRepository.delete(rate);
        } catch (Exception e) {
            throw new FigureException(FigureExceptionErrorCode.FIGURE_OPERATION_FAILED);
        }
    }

    //별점 모두 제거
    public void removeAllRates(Long figureId) {
        figureRateRepository.deleteAllByFigureId(figureId);
    }

    private Figure findFigureById(Long figureId) {
        return figureRepository.findById(figureId)
                .orElseThrow(() -> new EntityNotFoundException("Figure 없음 " + figureId));
    }
}
