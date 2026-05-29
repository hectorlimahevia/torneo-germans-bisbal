package com.ironhack.torneo_germans_bisbal_api.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tournament {
    @Id
    private Long id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String location;
}
