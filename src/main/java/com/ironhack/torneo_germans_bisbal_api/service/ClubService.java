package com.ironhack.torneo_germans_bisbal_api.service;

import com.ironhack.torneo_germans_bisbal_api.model.entity.Club;

import java.util.List;

public interface ClubService {
    //operaciones necesarias para club

    List<Club> getAllClubs();

    Club getClubById(Long id);

    Club createClub(Club Club);

    Club updateClub(Long id, Club club);

    void deleteClub(Long id );
}
