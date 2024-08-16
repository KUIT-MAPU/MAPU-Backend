package com.mapu.domain.figure.dao;

import com.mapu.domain.figure.domain.FigureTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FigureTagRepository extends JpaRepository<FigureTag, Long> {
    void deleteAllByFigureId(Long figureId);
    Optional<FigureTag> findByIdAndFigureId(Long id, Long figureId);
}
