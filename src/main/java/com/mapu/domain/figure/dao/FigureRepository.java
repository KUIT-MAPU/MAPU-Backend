package com.mapu.domain.figure.dao;

import com.mapu.domain.figure.domain.Figure;
import com.mapu.domain.figure.domain.FigureType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FigureRepository extends JpaRepository<Figure, Long> {

    List<Figure> findByMapId(Long mapId);

}