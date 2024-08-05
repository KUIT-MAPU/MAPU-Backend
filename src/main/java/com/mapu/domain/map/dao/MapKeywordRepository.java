package com.mapu.domain.map.dao;

import com.mapu.domain.map.domain.MapKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapKeywordRepository extends JpaRepository<MapKeyword, Long> {
    @Query("SELECT k.keyword FROM MapKeyword mk JOIN Keyword k ON mk.id = k.id WHERE mk.map.id = :mapId")
    List<String> findKeywordsByMapId(@Param("mapId") Long mapId);
}
