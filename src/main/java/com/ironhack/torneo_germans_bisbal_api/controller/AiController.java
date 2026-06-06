package com.ironhack.torneo_germans_bisbal_api.controller;

import com.ironhack.torneo_germans_bisbal_api.service.AiService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("/chat")
    public String chat(@RequestParam String message){
        return aiService.chat(message);
    }
}
