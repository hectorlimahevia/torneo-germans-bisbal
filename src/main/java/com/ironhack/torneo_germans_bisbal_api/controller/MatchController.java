package com.ironhack.torneo_germans_bisbal_api.controller;


import com.ironhack.torneo_germans_bisbal_api.dto.MatchRequestDTO;
import com.ironhack.torneo_germans_bisbal_api.dto.MatchResponseDTO;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Match;
import com.ironhack.torneo_germans_bisbal_api.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id);
    }

    @PostMapping
    public MatchResponseDTO createMatch(@RequestBody MatchRequestDTO dto) {
        return matchService.createMatch(dto);
    }

    @PutMapping("/{id}")
    public Match updateMatch(@PathVariable Long id, @RequestBody Match match){
        return matchService.updateMatch(id, match);
    }

    @DeleteMapping("/{id}")
    public void deleteMatch(@PathVariable Long id){
        matchService.deleteMatch(id);
    }
}
