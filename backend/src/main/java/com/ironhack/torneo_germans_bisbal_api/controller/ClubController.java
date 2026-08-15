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

    private final ClubService clubService;

    @GetMapping
    public List<Club> getAllClubs(){
        return clubService.getAllClubs();
    }

    @GetMapping("/{id}")
    public Club getClubById(@PathVariable Long id){
        return clubService.getClubById(id);
    }

    @PostMapping()
    public Club createClub(@RequestBody Club club){
        return clubService.createClub(club);
    }

    @PutMapping("/{id}")
    public Club updateClub(@PathVariable Long id, @RequestBody Club club){
        return clubService.updateClub(id, club);
    }

    @DeleteMapping("/{id}")
    public void deleteClub(@PathVariable Long id){
        clubService.deleteClub(id);
    }
}
