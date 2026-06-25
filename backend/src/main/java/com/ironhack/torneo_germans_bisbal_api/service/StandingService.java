package com.ironhack.torneo_germans_bisbal_api.service;

import com.ironhack.torneo_germans_bisbal_api.dto.StandingResponseDTO;
import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;

import java.util.List;

public interface StandingService {

    List<StandingResponseDTO> getStandingsByCategory(Category category);
}