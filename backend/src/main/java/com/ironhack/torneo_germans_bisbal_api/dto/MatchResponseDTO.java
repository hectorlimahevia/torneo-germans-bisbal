package com.ironhack.torneo_germans_bisbal_api.dto;

import com.ironhack.torneo_germans_bisbal_api.model.enums.MatchStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class MatchResponseDTO {

    private Long id;

    private String localTeamName;

    private String visitorTeamName;

    private String fieldName;

    private LocalDate matchDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer localTries;

    private Integer visitorTries;

    private MatchStatus status;

    private Integer roundNumber;
}