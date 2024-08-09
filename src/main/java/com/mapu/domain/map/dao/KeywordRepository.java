package com.mapu.domain.map.dao;

import com.mapu.domain.map.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    @Query(value = "SELECT k FROM Keyword k ORDER BY RAND() LIMIT :limit")
    List<Keyword> findRandomKeywords(@Param("limit") int limit);

}