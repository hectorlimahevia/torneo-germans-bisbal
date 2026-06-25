package com.ironhack.torneo_germans_bisbal_api.dto;

import com.ironhack.torneo_germans_bisbal_api.model.enums.MatchStatus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MatchRequestDTO {
    private Long localTeamId;

    private Long visitorTeamId;

    private Long fieldId;

    private LocalDate matchDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer localTries;

    private Integer visitorTries;

    private MatchStatus status;

    private Integer roundNumber;
}
