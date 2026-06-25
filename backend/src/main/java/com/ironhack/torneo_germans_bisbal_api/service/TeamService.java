package com.ironhack.torneo_germans_bisbal_api.service;


import com.ironhack.torneo_germans_bisbal_api.model.entity.Team;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TeamService {

    List<Team> getAllTeams();

    Team getTeamById(Long id);

    Team createTeam(Team team);

    Team updateTeam(Long id, Team team);

    public void deleteTeam(Long id);
}
