package com.ironhack.torneo_germans_bisbal_api.repository;

import com.ironhack.torneo_germans_bisbal_api.model.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
}
