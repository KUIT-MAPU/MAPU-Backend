package com.mapu.domain.map.dao;

import com.mapu.domain.map.application.response.MapListResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.mapu.domain.map.domain.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MapRepository extends JpaRepository<Map, Long> {
    @Query("SELECT m FROM Map m ORDER BY m.created_at DESC")
    List<Map> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT m FROM Map m ORDER BY FUNCTION('RAND')")
    List<Map> findAllByRandom(Pageable pageable);

    Map findById(long mapId);

    @Query("SELECT COUNT(m) FROM Map m WHERE m.user.id = :userId")
    int countMapsByUserId(@Param("userId") Long userId);

    @Query("SELECT m FROM Map m WHERE m.user.id = :otherUserId")
    List<Map> findOtherUserMapsByUserId(@Param("otherUserId") Long otherUserId, Pageable pageable);
}
