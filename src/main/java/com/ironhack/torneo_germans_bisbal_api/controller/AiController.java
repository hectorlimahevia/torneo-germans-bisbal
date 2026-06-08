package com.ironhack.torneo_germans_bisbal_api.controller;

import com.ironhack.torneo_germans_bisbal_api.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("/chat/{conversationId}")
    public String chatWithMemory(
            @PathVariable String conversationId,
            @RequestParam String message) {

        return aiService.chatWithMemory(
                conversationId,
                message);
    }

    @GetMapping("/summary/{category}")
    public String getCategorySummary(@PathVariable Category category) {

        return aiService.getCategorySummary(category);
    }
}
