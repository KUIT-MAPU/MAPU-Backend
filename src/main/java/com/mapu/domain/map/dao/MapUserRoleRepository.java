package com.mapu.domain.map.dao;

import com.mapu.domain.map.application.response.MapEditorResponseDTO;
import com.mapu.domain.map.domain.MapUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MapUserRoleRepository extends JpaRepository<MapUserRole, Long> {
    boolean existsByMapIdAndUserId(Long mapId, Long userId);

    @Query("SELECT new com.mapu.domain.map.application.response.MapEditorResponseDTO(u.id, u.nickname, mur.role, u.image) " +
            "FROM MapUserRole mur " +
            "LEFT JOIN mur.user u " +
            "WHERE mur.map.id = :mapId")
    Page<MapEditorResponseDTO> findMapEditor(@Param("mapId") Long mapId, PageRequest pageRequest);
}
