package com.ironhack.torneo_germans_bisbal_api.tools;

import com.ironhack.torneo_germans_bisbal_api.dto.StandingResponseDTO;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Rule;
import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;
import com.ironhack.torneo_germans_bisbal_api.service.RuleService;
import com.ironhack.torneo_germans_bisbal_api.service.StandingService;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Match;
import com.ironhack.torneo_germans_bisbal_api.repository.MatchRepository;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Team;
import com.ironhack.torneo_germans_bisbal_api.repository.TeamRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TournamentAiTools {

    private final RuleService ruleService;
    private final StandingService standingService;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;


    //herramienta para obtener las reglas
    @Tool(description = "Get all tournament rules")
    public List<Rule> getRules() {

        return ruleService.getAllRules();
    }

    //Herramienta para saber la clasificacion
    @Tool(description = """
            Get the standings table for a rugby category.
            Use this tool when the user asks about rankings,
            classification, leaders, positions or standings.
            """)
    public List<StandingResponseDTO> getStandingsByCategory(Category category) {
        return standingService.getStandingsByCategory(category);
    }

    //herramienta para partidos por categoria
    @Tool(description = """
            Get all rugby matches for a specific category.
            Use this tool when the user asks about fixtures,
            schedules, games, match times or when a category plays.
            """)
    public List<String> getMatchesByCategory(Category category) {
        return matchRepository.findAll()
                .stream()
                .filter(match -> match.getLocalTeam().getCategory() == category)
                .map(match ->
                        "Match " + match.getId() +
                                ": " + match.getLocalTeam().getName() +
                                " vs " + match.getVisitorTeam().getName() +
                                ", field: " + match.getField().getName() +
                                ", date: " + match.getMatchDate() +
                                ", start: " + match.getStartTime() +
                                ", end: " + match.getEndTime() +
                                ", status: " + match.getStatus() +
                                ", score: " + match.getLocalTries() +
                                "-" + match.getVisitorTries()
                )
                .toList();
    }

    //herramienta para informacion de equipos
    @Tool(description = """
            Get information about a rugby team by name.
            Use this tool when the user asks about a team,
            its category, club or general team information.
            """)
    public String getTeamInfo(String teamName) {

        return teamRepository.findAll()
                .stream()
                .filter(team -> team.getName()
                        .toLowerCase()
                        .contains(teamName.toLowerCase()))
                .findFirst()
                .map(team ->
                        "Team: " + team.getName() +
                                ", category: " + team.getCategory() +
                                ", club: " + team.getClub().getName() +
                                ", city: " + team.getClub().getCity() +
                                ", country: " + team.getClub().getCountry()
                )
                .orElse("No team found with name: " + teamName);
    }
}
