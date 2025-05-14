package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    boolean existsByTokenId(String tokenId);

    Optional<UserSession> findByTokenId(String tokenId);

}
