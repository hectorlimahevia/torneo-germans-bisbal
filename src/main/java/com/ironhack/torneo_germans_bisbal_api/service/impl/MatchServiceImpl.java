package com.ironhack.torneo_germans_bisbal_api.service.impl;

import com.ironhack.torneo_germans_bisbal_api.dto.MatchRequestDTO;
import com.ironhack.torneo_germans_bisbal_api.dto.MatchResponseDTO;
import com.ironhack.torneo_germans_bisbal_api.exception.ResourceNotFoundException;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Field;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Match;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Team;
import com.ironhack.torneo_germans_bisbal_api.repository.FieldRepository;
import com.ironhack.torneo_germans_bisbal_api.repository.MatchRepository;
import com.ironhack.torneo_germans_bisbal_api.repository.TeamRepository;
import com.ironhack.torneo_germans_bisbal_api.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public
class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

    private final TeamRepository teamRepository;

    private final FieldRepository fieldRepository;

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + id));
    }

    //metodo usando el dto
    @Override
    public MatchResponseDTO createMatch(MatchRequestDTO dto) {

        Team localTeam = teamRepository.findById(dto.getLocalTeamId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Local team not found"));

        Team visitorTeam = teamRepository.findById(dto.getVisitorTeamId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Visitor team not found"));

        Field field = fieldRepository.findById(dto.getFieldId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Field not found"));

        Match match = Match.builder()
                .localTeam(localTeam)
                .visitorTeam(visitorTeam)
                .field(field)
                .matchDate(dto.getMatchDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .localTries(dto.getLocalTries())
                .visitorTries(dto.getVisitorTries())
                .status(dto.getStatus())
                .roundNumber(dto.getRoundNumber())
                .build();

        Match savedMatch = matchRepository.save(match);
        return MatchResponseDTO.builder()
                .id(savedMatch.getId())
                .localTeamName(savedMatch.getLocalTeam().getName())
                .visitorTeamName(savedMatch.getVisitorTeam().getName())
                .fieldName(savedMatch.getField().getName())
                .matchDate(savedMatch.getMatchDate())
                .startTime(savedMatch.getStartTime())
                .endTime(savedMatch.getEndTime())
                .localTries(savedMatch.getLocalTries())
                .visitorTries(savedMatch.getVisitorTries())
                .status(savedMatch.getStatus())
                .roundNumber(savedMatch.getRoundNumber())
                .build();
    }

    @Override
    public Match updateMatch(Long id, Match match) {
        Match existingMatch = getMatchById(id);

        existingMatch.setLocalTeam(match.getLocalTeam());
        existingMatch.setVisitorTeam(match.getVisitorTeam());
        existingMatch.setField(match.getField());
        existingMatch.setMatchDate(match.getMatchDate());
        existingMatch.setStartTime(match.getStartTime());
        existingMatch.setEndTime(match.getEndTime());
        existingMatch.setLocalTries(match.getLocalTries());
        existingMatch.setVisitorTries(match.getVisitorTries());
        existingMatch.setStatus(match.getStatus());
        existingMatch.setRoundNumber(match.getRoundNumber());

        return matchRepository.save(existingMatch);
    }
    @Override
    public void deleteMatch(Long id) {
        getMatchById(id);

        matchRepository.deleteById(id);
    }
}
