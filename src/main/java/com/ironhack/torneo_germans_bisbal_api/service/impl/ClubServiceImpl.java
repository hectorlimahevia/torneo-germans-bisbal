package com.ironhack.torneo_germans_bisbal_api.service.impl;

import com.ironhack.torneo_germans_bisbal_api.model.entity.Club;
import com.ironhack.torneo_germans_bisbal_api.repository.ClubRepository;
import com.ironhack.torneo_germans_bisbal_api.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;

    //obtener todos los clubs
    @Override
    public List<Club> getAllClubs(){
        return clubRepository.findAll();
    }

    //Obtenr club por id
    public Club getClubById(Long id){
        return clubRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Club not found wit id: " + id));
    }

    //Crear un nuevo club
    @Override
    public Club createClub(Club club) {
        return clubRepository.save(club);
    }

    //Modificar club
    @Override
    public Club updateClub(Long id, Club club ){
        Club existingClub = getClubById(id);

        existingClub.setName(club.getName());
        existingClub.setCity(club.getCity());
        existingClub.setCountry(club.getCountry());

        return clubRepository.save(existingClub);
    }

    //Eliminar club
    @Override
    public void deleteClub(Long id){
        getClubById(id);

       clubRepository.deleteById(id);
    }
}
