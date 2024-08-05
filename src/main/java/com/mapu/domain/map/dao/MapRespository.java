package com.mapu.domain.map.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.mapu.domain.map.domain.Map;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface MapRespository extends JpaRepository<Map, Long> {
    @Query("SELECT m FROM Map m ORDER BY m.created_at DESC")
    List<Map> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT m FROM Map m ORDER BY FUNCTION('RAND')")
    List<Map> findAllByRandom(Pageable pageable);
}
