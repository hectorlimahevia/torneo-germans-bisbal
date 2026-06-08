package com.ironhack.torneo_germans_bisbal_api.tools;

import com.ironhack.torneo_germans_bisbal_api.dto.StandingResponseDTO;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Rule;
import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;
import com.ironhack.torneo_germans_bisbal_api.service.RuleService;
import com.ironhack.torneo_germans_bisbal_api.service.StandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TournamentAiTools {

    private final RuleService ruleService;
    private final StandingService standingService;


    //herramienta para obtener las reglas
    @Tool(description = "Get all tournament rules")
    public List<Rule> getRules() {

        return ruleService.getAllRules();
    }

    //Herramienta para saber la clasificacion
    @Tool(description = """
        Get the standings table for a rugby category.
        Use this tool when the user asks about rankings,
        classification, leaders, positions or standings.
        """)
    public List<StandingResponseDTO> getStandingsByCategory(Category category) {
        return standingService.getStandingsByCategory(category);
    }


}
