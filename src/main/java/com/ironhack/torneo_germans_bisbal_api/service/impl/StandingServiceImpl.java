package com.ironhack.torneo_germans_bisbal_api.service.impl;

import com.ironhack.torneo_germans_bisbal_api.dto.StandingResponseDTO;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Match;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Team;
import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;
import com.ironhack.torneo_germans_bisbal_api.model.enums.MatchStatus;
import com.ironhack.torneo_germans_bisbal_api.repository.MatchRepository;
import com.ironhack.torneo_germans_bisbal_api.repository.TeamRepository;
import com.ironhack.torneo_germans_bisbal_api.service.StandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StandingServiceImpl implements StandingService {

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    @Override
    public List<StandingResponseDTO> getStandingsByCategory(Category category) {

        List<Team> teams = teamRepository.findAll().stream()
                .filter(team -> team.getCategory() == category)
                .toList();

        List<StandingResponseDTO> standings = new ArrayList<>();

        List<Match> matches = matchRepository.findAll();

        for (Team team : teams) {

            int played = 0;
            int won = 0;
            int drawn = 0;
            int lost = 0;

            int triesFor = 0;
            int triesAgainst = 0;

            int offensiveBonus = 0;
            int defensiveBonus = 0;

            int totalPoints = 0;

            for (Match match : matches) {

                if (match.getStatus() != MatchStatus.FINISHED) {
                    continue;
                }

                boolean isLocal = match.getLocalTeam().getId().equals(team.getId());
                boolean isVisitor = match.getVisitorTeam().getId().equals(team.getId());

                if (!isLocal && !isVisitor) {
                    continue;
                }

                played++;

                int teamTries;
                int opponentTries;

                if (isLocal) {
                    teamTries = match.getLocalTries();
                    opponentTries = match.getVisitorTries();
                } else {
                    teamTries = match.getVisitorTries();
                    opponentTries = match.getLocalTries();
                }

                triesFor += teamTries;
                triesAgainst += opponentTries;

                if (teamTries > opponentTries) {
                    won++;
                    totalPoints += 3;
                } else if (teamTries == opponentTries) {
                    drawn++;
                    totalPoints += 1;
                } else {
                    lost++;
                }

                if (teamTries > 3) {
                    offensiveBonus++;
                    totalPoints++;
                }

                if (teamTries < opponentTries && opponentTries - teamTries == 1) {
                    defensiveBonus++;
                    totalPoints++;
                }
            }

            int triesDifference = triesFor - triesAgainst;

            StandingResponseDTO standing = StandingResponseDTO.builder()
                    .teamId(team.getId())
                    .teamName(team.getName())
                    .category(team.getCategory())
                    .played(played)
                    .won(won)
                    .drawn(drawn)
                    .lost(lost)
                    .triesFor(triesFor)
                    .triesAgainst(triesAgainst)
                    .triesDifference(triesDifference)
                    .offensiveBonus(offensiveBonus)
                    .defensiveBonus(defensiveBonus)
                    .totalPoints(totalPoints)
                    .build();

            standings.add(standing);
        }

        //Ordenando standing por puntuacion
        standings.sort(
                Comparator.comparingInt(StandingResponseDTO::getTotalPoints)
                        .thenComparingInt(StandingResponseDTO::getTriesDifference)
                        .thenComparingInt(StandingResponseDTO::getTriesFor)
                        .reversed()
        );

        for (int i = 0; i < standings.size(); i++) {
            standings.get(i).setRankPosition(i + 1);
        }

        return standings;
    }
}