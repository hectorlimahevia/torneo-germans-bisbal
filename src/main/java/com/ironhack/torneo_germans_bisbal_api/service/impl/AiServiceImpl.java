package com.ironhack.torneo_germans_bisbal_api.service.impl;

import com.ironhack.torneo_germans_bisbal_api.service.AiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    public AiServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String chat(String message) {
        return chatClient
                .prompt(message)
                .call()
                .content();
    }
}