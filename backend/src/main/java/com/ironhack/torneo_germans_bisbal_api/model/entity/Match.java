package com.ironhack.torneo_germans_bisbal_api.model.entity;


import com.ironhack.torneo_germans_bisbal_api.model.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Team localTeam;

    @ManyToOne
    private Team visitorTeam;

    @ManyToOne
    private Field field;

    private LocalDate matchDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer localTries;

    private Integer visitorTries;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    private Integer roundNumber;

}
