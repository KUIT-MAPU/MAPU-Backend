package com.mapu.domain.follow.dao;

import com.mapu.domain.follow.domain.Follow;
import com.mapu.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
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

    // 사용자의 팔로워 수 조회
    long countByFollowing(User following);

    // 사용자의 팔로잉 수 조회
    long countByFollower(User follower);

    // 팔로우 관계 삭제
    void deleteByFollowerAndFollowing(User follower, User following);

    // 특정 팔로우 관계 조회
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}