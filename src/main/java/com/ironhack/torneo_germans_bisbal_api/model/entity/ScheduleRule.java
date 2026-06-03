package com.ironhack.torneo_germans_bisbal_api.model.entity;

import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("SCHEDULE")
@Getter
@Setter
@NoArgsConstructor
public class ScheduleRule extends Rule {

    @Enumerated(EnumType.STRING)
    private Category category;

    public ScheduleRule(Long id, String title, String description, Category category) {
        super(id, title, description);
        this.category = category;
    }
}