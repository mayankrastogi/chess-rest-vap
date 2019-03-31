package com.mayankrastogi.cs441.hw4.chessservice;

import com.mayankrastogi.cs441.hw4.chessservice.core.PlayerColor;
import com.mayankrastogi.cs441.hw4.chessservice.engines.ChessEngine;
import com.mayankrastogi.cs441.hw4.chessservice.engines.javaopenchess.JavaOpenChessEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class PlayerControllerTest {

    private static final String SERVER_URL = "http://localhost:8080";

    @TestConfiguration
    static class PlayerControllerTestConfigurationContext {
        @Bean
        public ChessEngine chessEngine() {
            return new JavaOpenChessEngine();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void play_SucceedsForBlackClientAIvsWhiteServerAI() throws Exception {
        mockMvc.perform(post("/play")
                    .param("serverURL", SERVER_URL)
                    .param("serverPlayerAILevel", "1")
                    .param("playerName", "Test Player")
                    .param("playerColor", PlayerColor.BLACK.toString())
                    .param("playerAILevel", "2")
                    .param("maxMoves", "50")
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outcome.moves.length()", greaterThanOrEqualTo(50)));
    }

    @Test
    public void play_SucceedsForWhiteClientAIvsBlackServerAI() throws Exception {
        mockMvc.perform(post("/play")
                    .param("serverURL", SERVER_URL)
                    .param("serverPlayerAILevel", "1")
                    .param("playerName", "Test Player")
                    .param("playerColor", PlayerColor.WHITE.toString())
                    .param("playerAILevel", "2")
                    .param("maxMoves", "50")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outcome.moves.length()", greaterThanOrEqualTo(50)));
    }

    @Test
    public void play_FailsForUnsupportedParameters() throws Exception {
        mockMvc.perform(post("/play")
                    .param("serverURL", SERVER_URL)
                    .param("serverPlayerAILevel", "1")
                    .param("playerName", "Test Player")
                    .param("playerColor", "Invalid Player Color")
                    .param("playerAILevel", "2")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
