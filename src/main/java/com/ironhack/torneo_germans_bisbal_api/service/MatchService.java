package com.ironhack.torneo_germans_bisbal_api.service;

import com.ironhack.torneo_germans_bisbal_api.dto.MatchRequestDTO;
import com.ironhack.torneo_germans_bisbal_api.dto.MatchResponseDTO;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Match;

import java.util.List;

public interface MatchService {

    List<Match> getAllMatches();

    Match getMatchById(Long id);

    MatchResponseDTO createMatch(MatchRequestDTO dto);

    MatchResponseDTO updateMatch(Long id, MatchRequestDTO dto);

    void deleteMatch(Long id);
}