package com.ironhack.torneo_germans_bisbal_api.repository;

import com.ironhack.torneo_germans_bisbal_api.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
