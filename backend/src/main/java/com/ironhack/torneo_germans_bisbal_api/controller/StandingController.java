package com.ironhack.torneo_germans_bisbal_api.controller;

import com.ironhack.torneo_germans_bisbal_api.dto.StandingResponseDTO;
import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;
import com.ironhack.torneo_germans_bisbal_api.service.StandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/standings")
@RequiredArgsConstructor
public class StandingController {

    private final StandingService standingService;

    @GetMapping("/{category}")
    public List<StandingResponseDTO> getStandingsByCategory(@PathVariable Category category){
        return standingService.getStandingsByCategory(category);
    }
}
