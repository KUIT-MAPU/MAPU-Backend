package com.mapu.domain.home.dao;

import com.mapu.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EditorRepository extends JpaRepository<User, Long> {



    @Query(value = "SELECT u FROM User u " +
            "WHERE u.id NOT IN (SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId) " +
            "AND u.id IN (SELECT DISTINCT m.user.id FROM Map m) " +
            "AND u.id != :userId " +
            "ORDER BY RAND() " +
            "LIMIT :limit")
    List<User> findRandomEditorsExcludingFollowed(@Param("userId") Long userId, @Param("limit") int limit);

    @Query(value = "SELECT u FROM User u " +
            "WHERE u.id IN (SELECT DISTINCT m.user.id FROM Map m) " +
            "ORDER BY RAND() " +
            "LIMIT :limit")
    List<User> findRandomEditors(@Param("limit") int limit);
}