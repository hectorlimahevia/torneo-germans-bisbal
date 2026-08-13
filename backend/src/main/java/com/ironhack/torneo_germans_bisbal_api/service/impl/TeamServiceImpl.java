package com.ironhack.torneo_germans_bisbal_api.service.impl;

import com.ironhack.torneo_germans_bisbal_api.exception.ResourceNotFoundException;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Club;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Team;
import com.ironhack.torneo_germans_bisbal_api.repository.ClubRepository;
import com.ironhack.torneo_germans_bisbal_api.repository.TeamRepository;
import com.ironhack.torneo_germans_bisbal_api.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ClubRepository clubRepository;

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
        team.setClub(resolveClub(team));

        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(Long id, Team team) {
        Team existingTeam = getTeamById(id);

        existingTeam.setName(team.getName());
        existingTeam.setCategory(team.getCategory());
        existingTeam.setClub(resolveClub(team));

        return teamRepository.save(existingTeam);
    }

    private Club resolveClub(Team team) {
        if (team.getClub() == null || team.getClub().getId() == null) {
            throw new IllegalArgumentException("A team must be assigned to an existing club");
        }

        Long clubId = team.getClub().getId();

        return clubRepository.findById(clubId)
                .orElseThrow(() -> new ResourceNotFoundException("Club not found with id: " + clubId));
    }

    @Override
    public void deleteTeam(Long id) {
        getTeamById(id);
        teamRepository.deleteById(id);
    }
}