package com.ironhack.torneo_germans_bisbal_api.controller;

import com.ironhack.torneo_germans_bisbal_api.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("/chat")
    public String chatWithMemory(
            @RequestParam String message, Authentication authentication) {
        String username = authentication.getName();

        return aiService.chatWithMemory(
                username,
                message);
    }

    @GetMapping("/summary/{category}")
    public String getCategorySummary(@PathVariable Category category) {

        return aiService.getCategorySummary(category);
    }
}
