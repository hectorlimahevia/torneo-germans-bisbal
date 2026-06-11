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

        validateTeams(localTeam, visitorTeam);
        validateScore(dto);
        validateTime(dto);
        validateAvailability(dto, localTeam, visitorTeam, field);

        //creando el partido
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
    public MatchResponseDTO updateMatch(Long id, MatchRequestDTO dto) {

        Match existingMatch = getMatchById(id);

        Team localTeam = teamRepository.findById(dto.getLocalTeamId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Local team not found"));

        Team visitorTeam = teamRepository.findById(dto.getVisitorTeamId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Visitor team not found"));

        Field field = fieldRepository.findById(dto.getFieldId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Field not found"));

        validateTeams(localTeam, visitorTeam);
        validateScore(dto);
        validateTime(dto);

        existingMatch.setLocalTeam(localTeam);
        existingMatch.setVisitorTeam(visitorTeam);
        existingMatch.setField(field);
        existingMatch.setMatchDate(dto.getMatchDate());
        existingMatch.setStartTime(dto.getStartTime());
        existingMatch.setEndTime(dto.getEndTime());
        existingMatch.setLocalTries(dto.getLocalTries());
        existingMatch.setVisitorTries(dto.getVisitorTries());
        existingMatch.setStatus(dto.getStatus());
        existingMatch.setRoundNumber(dto.getRoundNumber());

        Match updatedMatch = matchRepository.save(existingMatch);

        return MatchResponseDTO.builder()
                .id(updatedMatch.getId())
                .localTeamName(updatedMatch.getLocalTeam().getName())
                .visitorTeamName(updatedMatch.getVisitorTeam().getName())
                .fieldName(updatedMatch.getField().getName())
                .matchDate(updatedMatch.getMatchDate())
                .startTime(updatedMatch.getStartTime())
                .endTime(updatedMatch.getEndTime())
                .localTries(updatedMatch.getLocalTries())
                .visitorTries(updatedMatch.getVisitorTries())
                .status(updatedMatch.getStatus())
                .roundNumber(updatedMatch.getRoundNumber())
                .build();
    }

    @Override
    public void deleteMatch(Long id) {
        getMatchById(id);

        matchRepository.deleteById(id);
    }

    /**********************************metodos privados************************/
    private void validateTeams(Team localTeam, Team visitorTeam) {
        if (localTeam.getId().equals(visitorTeam.getId())) {
            throw new IllegalArgumentException("A team cannot play against itself");
        }

        if (localTeam.getCategory() != visitorTeam.getCategory()) {
            throw new IllegalArgumentException("Teams must belong to the same category");
        }
    }

    private void validateScore(MatchRequestDTO dto) {
        if (dto.getLocalTries() < 0 || dto.getVisitorTries() < 0) {
            throw new IllegalArgumentException("Tries cannot be negative");
        }
    }

    private void validateTime(MatchRequestDTO dto) {
        if (dto.getEndTime().isBefore(dto.getStartTime())
                || dto.getEndTime().equals(dto.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    private boolean timeOverlaps(MatchRequestDTO dto, Match existingMatch) {
        return dto.getStartTime().isBefore(existingMatch.getEndTime())
                && dto.getEndTime().isAfter(existingMatch.getStartTime());
    }

    private void validateAvailability(MatchRequestDTO dto, Team localTeam, Team visitorTeam, Field field) {
        List<Match> existingMatches = matchRepository.findAll();

        for (Match existingMatch : existingMatches) {
            boolean sameDate = existingMatch.getMatchDate().equals(dto.getMatchDate());

            if (!sameDate) {
                continue;
            }

            boolean overlaps = timeOverlaps(dto, existingMatch);

            if (!overlaps) {
                continue;
            }

            boolean sameField = existingMatch.getField().getId().equals(field.getId());

            if (sameField) {
                throw new IllegalArgumentException("Field is already occupied at this time");
            }

            boolean localTeamAlreadyPlaying =
                    existingMatch.getLocalTeam().getId().equals(localTeam.getId())
                            || existingMatch.getVisitorTeam().getId().equals(localTeam.getId());

            boolean visitorTeamAlreadyPlaying =
                    existingMatch.getLocalTeam().getId().equals(visitorTeam.getId())
                            || existingMatch.getVisitorTeam().getId().equals(visitorTeam.getId());

            if (localTeamAlreadyPlaying || visitorTeamAlreadyPlaying) {
                throw new IllegalArgumentException("One of the teams is already playing at this time");
            }
        }
    }
}
