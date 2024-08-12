package com.mapu.domain.follow.dao;

import com.mapu.domain.follow.domain.Follow;
import com.mapu.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 팔로우 관계 존재 여부 확인 ?
    boolean existsByFollowerAndFollowing(User follower, User following);

    // 사용자의 팔로워 목록 조회
    List<Follow> findByFollowing(User following);

    // 사용자의 팔로잉 목록 조회
    List<Follow> findByFollower(User follower);

    // 특정 팔로우 관계 조회
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :userId")
    int countFollowerByUserId(@Param("userId") long userId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :userId")
    int countFollowingByUserId(@Param("userId") long userId);

    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId")
    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);
}