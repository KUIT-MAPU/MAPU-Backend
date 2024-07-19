package com.mapu.infra.oauth.dao;

import com.mapu.infra.oauth.domain.OAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthRepository extends JpaRepository<OAuth, Long> {
}
