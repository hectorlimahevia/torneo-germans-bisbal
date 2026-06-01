package com.ironhack.torneo_germans_bisbal_api.controller;

import com.ironhack.torneo_germans_bisbal_api.model.entity.Club;
import com.ironhack.torneo_germans_bisbal_api.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {
    //inyectando dependencias
    private final ClubService clubService;

    //obtener todos los clubs
    @GetMapping
    public List<Club> getAllClubs(){
        return clubService.getAllClubs();
    }

    //Añadir club ruta protegida por Admin
    @PostMapping()
    public Club createClub(@RequestBody Club club){
        return clubService.createClub(club);
    }

    //obtener club por id
    @GetMapping("/{id}")
    public Club getClubById(@PathVariable Long id){
        return clubService.getClubById(id);
    }
}
