package com.ironhack.torneo_germans_bisbal_api.repository;

import com.ironhack.torneo_germans_bisbal_api.model.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
