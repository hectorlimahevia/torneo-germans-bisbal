package com.ironhack.torneo_germans_bisbal_api.repository;

import com.ironhack.torneo_germans_bisbal_api.model.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, Long> {
}
