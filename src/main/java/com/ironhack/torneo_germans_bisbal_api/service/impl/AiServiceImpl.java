package com.ironhack.torneo_germans_bisbal_api.service.impl;

import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;
import com.ironhack.torneo_germans_bisbal_api.service.AiService;
import com.ironhack.torneo_germans_bisbal_api.dto.StandingResponseDTO;
import com.ironhack.torneo_germans_bisbal_api.service.StandingService;
import com.ironhack.torneo_germans_bisbal_api.tools.TournamentAiTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;

import java.util.List;

@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    private final StandingService standingService;

    private final TournamentAiTools tournamentAiTools;

    public AiServiceImpl(
            ChatClient.Builder chatClientBuilder,
            StandingService standingService,
            TournamentAiTools tournamentAiTools,
            ChatMemory chatMemory) {

        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();

        this.standingService = standingService;
        this.tournamentAiTools = tournamentAiTools;
    }

    @Override
    public String chatWithMemory(String conversationId, String message) {

        return chatClient
                .prompt()
                .system("""
                    Eres la IA oficial del Torneo Germans Bisbal UES.

                    Ayudas a familias, jugadores y organizadores.

                    Puedes consultar reglas, clasificaciones, equipos y partidos utilizando las herramientas disponibles.

                    Si el usuario pregunta por deportes distintos al rugby,
                    responde amablemente que este asistente está especializado
                    en el torneo de rugby UES.
                    """)
                .user(message)
                .advisors(a -> a.param(
                        ChatMemory.CONVERSATION_ID,
                        conversationId))
                .tools(tournamentAiTools)
                .call()
                .content();
    }

    @Override
    public String getCategorySummary(Category category) {

        List<StandingResponseDTO> standings =
                standingService.getStandingsByCategory(category);

        StringBuilder prompt = new StringBuilder();

        prompt.append("Eres un asistente experto en torneos de rugby infantil.\n");
        prompt.append("Analiza la siguiente clasificación de la categoría ")
                .append(category)
                .append(".\n");
        prompt.append("Haz un resumen breve, claro y útil para familias y organización.\n");
        prompt.append("Indica líder, equipos perseguidores y situación general.\n\n");

        for (StandingResponseDTO standing : standings) {
            prompt.append("Posición: ").append(standing.getRankPosition()).append("\n");
            prompt.append("Equipo: ").append(standing.getTeamName()).append("\n");
            prompt.append("Partidos jugados: ").append(standing.getPlayed()).append("\n");
            prompt.append("Victorias: ").append(standing.getWon()).append("\n");
            prompt.append("Empates: ").append(standing.getDrawn()).append("\n");
            prompt.append("Derrotas: ").append(standing.getLost()).append("\n");
            prompt.append("Tries a favor: ").append(standing.getTriesFor()).append("\n");
            prompt.append("Tries en contra: ").append(standing.getTriesAgainst()).append("\n");
            prompt.append("Diferencia de tries: ").append(standing.getTriesDifference()).append("\n");
            prompt.append("Puntos totales: ").append(standing.getTotalPoints()).append("\n\n");
        }

        return chatClient
                .prompt(prompt.toString())
                .call()
                .content();
    }
}