package com.ironhack.torneo_germans_bisbal_api.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("SCORING")
@Getter
@Setter
@NoArgsConstructor
public class ScoringRule extends Rule {

    private Integer points;

    public ScoringRule(Long id, String title, String description, Integer points) {
        super(id, title, description);
        this.points = points;
    }
}