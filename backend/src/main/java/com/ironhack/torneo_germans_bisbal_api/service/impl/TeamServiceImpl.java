package com.ironhack.torneo_germans_bisbal_api.service.impl;

import com.ironhack.torneo_germans_bisbal_api.exception.ResourceNotFoundException;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Team;
import com.ironhack.torneo_germans_bisbal_api.repository.TeamRepository;
import com.ironhack.torneo_germans_bisbal_api.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
    }

    @Override
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(Long id, Team team) {
        Team existingTeam = getTeamById(id);

        existingTeam.setName(team.getName());
        existingTeam.setCategory(team.getCategory());
        existingTeam.setClub(team.getClub());

        return teamRepository.save(existingTeam);
    }

    @Override
    public void deleteTeam(Long id) {
        getTeamById(id);
        teamRepository.deleteById(id);
    }
}