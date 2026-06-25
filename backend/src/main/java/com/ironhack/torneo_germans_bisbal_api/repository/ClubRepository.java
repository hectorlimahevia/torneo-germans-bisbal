package com.ironhack.torneo_germans_bisbal_api.repository;


import com.ironhack.torneo_germans_bisbal_api.model.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
}
