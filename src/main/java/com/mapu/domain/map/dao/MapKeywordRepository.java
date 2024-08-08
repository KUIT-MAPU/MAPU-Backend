package com.mapu.domain.map.dao;

import com.mapu.domain.map.domain.MapKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapKeywordRepository extends JpaRepository<MapKeyword, Long> {
    @Query("SELECT k.keyword FROM Keyword k join MapKeyword mk on mk.keyword.id = k.id WHERE mk.map.id = :mapId")
    List<String> findKeywordsByMapId(@Param("mapId") Long mapId);
}
