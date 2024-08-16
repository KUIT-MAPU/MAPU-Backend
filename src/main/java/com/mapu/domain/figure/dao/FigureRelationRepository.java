package com.mapu.domain.figure.dao;

import com.mapu.domain.figure.domain.Figure;
import com.mapu.domain.figure.domain.FigureRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FigureRelationRepository extends JpaRepository<FigureRelation, Long> {

    //연결관계 하나 삭제
    void deleteByFigureIdAndFigure2Id(Long figureId, Long relatedFigureId);
    // 연결관계 모두 삭제
    void deleteAllByFigureId(Long figureId);
}

