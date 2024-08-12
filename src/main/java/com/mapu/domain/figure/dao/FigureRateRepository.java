package com.mapu.domain.figure.dao;

import com.mapu.domain.figure.domain.FigureRate;
import com.mapu.domain.figure.domain.FigureTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FigureRateRepository extends JpaRepository<FigureRate, Long> {

    void deleteAllByFigureId(Long figureId);
    Optional<FigureRate> findByIdAndFigureId(Long id, Long figureId);

}