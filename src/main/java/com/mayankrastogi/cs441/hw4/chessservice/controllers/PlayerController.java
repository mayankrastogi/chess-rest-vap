package com.mayankrastogi.cs441.hw4.chessservice.controllers;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;
import com.mayankrastogi.cs441.hw4.chessservice.core.PlayerColor;
import com.mayankrastogi.cs441.hw4.chessservice.core.dto.ChessMoveDTO;
import com.mayankrastogi.cs441.hw4.chessservice.engines.ChessEngine;
import com.mayankrastogi.cs441.hw4.chessservice.restclients.ChessAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.HashMap;

@RestController
public class PlayerController {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerController.class);

    private ChessEngine chessEngine;

    @Autowired
    public void setChessEngine(ChessEngine chessEngine) {
        this.chessEngine = chessEngine;
    }

    @PostMapping("/play")
    public HashMap<String, Object> play(
            @RequestParam String serverURL,
            @RequestParam int serverPlayerAILevel,
            @RequestParam String playerName,
            @RequestParam PlayerColor playerColor,
            @RequestParam int playerAILevel) {

        LOG.trace(String.format(
                "play(serverURL=%s, serverPlayerAILevel=%s, playerName=%s, playerColor=%s, playerAILevel=%s)",
                serverURL, serverPlayerAILevel, playerName, playerColor, playerAILevel));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        ChessAPI chessAPI = retrofit.create(ChessAPI.class);

        try {
            Response<ChessMoveDTO> newGameResponse = chessAPI.newGame(playerName, playerColor, serverPlayerAILevel)
                    .execute();

            if (newGameResponse.isSuccessful()) {
                ChessMoveDTO newGameDTO = newGameResponse.body();

                // The server player's color is opposite of our color
                PlayerColor serverPlayer = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

                // Create a local copy of the chess game
                ChessEngine clientGame = chessEngine.newGame(
                        "Server Player",
                        serverPlayer,
                        playerAILevel
                );

                // If the server responded with first move, make that move in our local game
                if (newGameDTO.serverMove != null) {
                    clientGame.makeMove(newGameDTO.serverMove);
                }
                // Otherwise, we should make the first move
                else {
                    clientGame.makeMove(clientGame.getNextMove());
                }

                // Keep playing until the game ends
                while (!clientGame.hasGameEnded()) {
                    // The last move in local game will be our move, based on the last move made by the server
                    ChessMove nextMove = clientGame.getMoveHistory().peek();

                    // Tell the server to make the move we thought of
                    Response<ChessMoveDTO> makeMoveResponse = chessAPI.makeMove(newGameDTO.gameID, nextMove).execute();

                    if (makeMoveResponse.isSuccessful()) {
                        ChessMoveDTO makeMoveDTO = makeMoveResponse.body();

                        // Make the move made by the server on our local game
                        clientGame.makeMove(makeMoveDTO.serverMove);
                    } else {
                        LOG.error("Error making a move: " + makeMoveResponse.errorBody().string());
                        break;
                    }
                }
                LOG.info("Game Ended!");
            } else {
                LOG.error("Error creating new game: " + newGameResponse.errorBody().string());
            }
        } catch (IOException e) {
            LOG.error("An error occured while playing the game with chess service at " + serverURL, e);
        }

        return null;
    }
}
