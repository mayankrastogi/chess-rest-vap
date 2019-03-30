package com.mayankrastogi.cs441.hw4.chessservice.controllers;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;
import com.mayankrastogi.cs441.hw4.chessservice.core.PlayerColor;
import com.mayankrastogi.cs441.hw4.chessservice.core.dto.ChessMoveDTO;
import com.mayankrastogi.cs441.hw4.chessservice.core.dto.ChessPlayerDTO;
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

import static com.mayankrastogi.cs441.hw4.chessservice.engines.ChessEngine.AI_PLAYER_NAME;

/**
 * Exposes an end-point to play chess with another instance of this chess service.
 */
@RestController
public class PlayerController {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerController.class);

    private ChessEngine chessEngine;

    /**
     * Setter for injecting the chess engine.
     *
     * @param chessEngine The chess engine.
     */
    @Autowired
    public void setChessEngine(ChessEngine chessEngine) {
        this.chessEngine = chessEngine;
    }

    /**
     * Play AI vs AI chess with another instance of this chess service.
     * <p>
     * WARNING: Do NOT use the same AI level for both the server and the player, otherwise the game will go on forever.
     * There is no limit on the maximum number of moves as of now.
     *
     * @param serverURL           URL of the chess service.
     * @param serverPlayerAILevel AI level of the server player.
     * @param playerName          The name by which this AI player should be identified.
     * @param playerColor         The color to use for this AI player.
     * @param playerAILevel       The AI level of this player.
     * @return The results of the AI vs AI chess game.
     */
    @PostMapping("/play")
    public ChessPlayerDTO play(
            @RequestParam String serverURL,
            @RequestParam int serverPlayerAILevel,
            @RequestParam String playerName,
            @RequestParam PlayerColor playerColor,
            @RequestParam int playerAILevel) {

        LOG.trace(String.format(
                "play(serverURL=%s, serverPlayerAILevel=%s, playerName=%s, playerColor=%s, playerAILevel=%s)",
                serverURL, serverPlayerAILevel, playerName, playerColor, playerAILevel));

        // Configure retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        // Generate a REST client using the ChessAPI interface
        ChessAPI chessAPI = retrofit.create(ChessAPI.class);

        // The server player's color is opposite of our color
        PlayerColor serverPlayerColor = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

        ChessPlayerDTO response = new ChessPlayerDTO();
        response.playerName = playerName;
        response.playerColor = playerColor;
        response.playerAILevel = playerAILevel;
        response.serverPlayerName = AI_PLAYER_NAME;
        response.serverPlayerColor = serverPlayerColor;
        response.serverPlayerAILevel = serverPlayerAILevel;
        response.serverURL = serverURL;

        try {
            // Make the request to start a new game with the server
            Response<ChessMoveDTO> newGameResponse = chessAPI.newGame(playerName, playerColor, serverPlayerAILevel)
                    .execute();
            LOG.debug("Response received from newGame: " + newGameResponse);

            if (newGameResponse.isSuccessful()) {
                ChessMoveDTO newGameDTO = newGameResponse.body();

                // Update our response
                response.outcome = newGameDTO.status;

                // Create a local copy of the chess game
                ChessEngine clientGame = chessEngine.newGame(
                        "Server Player",
                        serverPlayerColor,
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

                boolean hasGameEnded = newGameDTO.status.hasGameEnded;

                // Keep playing until the game ends
                while (!hasGameEnded) {
                    // The last move in local game will be our move, based on the last move made by the server
                    ChessMove nextMove = clientGame.getMoveHistory().peek();

                    // Tell the server to make the move we thought of
                    Response<ChessMoveDTO> makeMoveResponse = chessAPI.makeMove(newGameDTO.gameID, nextMove).execute();

                    if (makeMoveResponse.isSuccessful()) {
                        ChessMoveDTO makeMoveDTO = makeMoveResponse.body();
                        LOG.debug("Response received from makeMove: " + makeMoveResponse);

                        // If the game has ended, we do not need to make a move
                        if (makeMoveDTO.status.hasGameEnded) {
                            hasGameEnded = true;
                        } else {
                            // Make the move made by the server on our local game
                            clientGame.makeMove(makeMoveDTO.serverMove);
                        }

                        // Update our response
                        response.outcome = makeMoveDTO.status;
                    } else {
                        hasGameEnded = true;

                        String errorMessage = "Error making a move: " + makeMoveResponse.errorBody().string();
                        LOG.error(errorMessage);
                        throw new RuntimeException(errorMessage);
                    }
                }
                LOG.info("Game Ended!");
            } else {
                String errorMessage = "Error creating new game: " + newGameResponse.errorBody().string();
                LOG.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            String errorMessage = "An error occurred while playing the game with chess service at " + serverURL;
            LOG.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }

        return response;
    }
}
