package com.ironhack.torneo_germans_bisbal_api.service;

import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;

public interface AiService {

    String chatWithMemory(String conversationId, String message);

    String getCategorySummary(Category category);
}
