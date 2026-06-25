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

    //obtener club por id
    @GetMapping("/{id}")
    public Club getClubById(@PathVariable Long id){
        return clubService.getClubById(id);
    }

    //Añadir club ruta protegida por Admin
    @PostMapping()
    public Club createClub(@RequestBody Club club){
        return clubService.createClub(club);
    }

    //Modificar club ruta protegida por Admin
    @PutMapping("/{id}")
    public Club updateClub(@PathVariable Long id, @RequestBody Club club){
        return clubService.updateClub(id, club);
    }

    //Eliminar club ruta protegida por Admin
    @DeleteMapping("/{id}")
    public void deleteClub(@PathVariable Long id){
        clubService.deleteClub(id);
    }
}
