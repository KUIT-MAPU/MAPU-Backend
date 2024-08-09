package com.mapu.domain.user.dao;

import com.mapu.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);
    boolean existsByProfileId(String profileId);
    boolean existsByEmail(String email);
    User findById(long id);
    User findByEmail(String Email);

    User findByNickname(String nickname);
}
