package com.ironhack.torneo_germans_bisbal_api.dto;

import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StandingResponseDTO {

    private Long teamId;

    private String teamName;

    private Category category;

    private Integer played;

    private Integer won;

    private Integer drawn;

    private Integer lost;

    private Integer triesFor;

    private Integer triesAgainst;

    private Integer triesDifference;

    private Integer offensiveBonus;

    private Integer defensiveBonus;

    private Integer totalPoints;

    private Integer rankPosition;

}
