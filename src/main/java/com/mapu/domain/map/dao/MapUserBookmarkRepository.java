package com.mapu.domain.map.dao;

import com.mapu.domain.map.domain.MapUserBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapUserBookmarkRepository extends JpaRepository<MapUserBookmark, Long> {
    MapUserBookmark findByUserIdAndMapId(Long userId, Long mapId);
}
