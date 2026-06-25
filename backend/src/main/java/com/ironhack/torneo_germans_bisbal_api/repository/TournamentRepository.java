package com.ironhack.torneo_germans_bisbal_api.repository;

import com.ironhack.torneo_germans_bisbal_api.model.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
