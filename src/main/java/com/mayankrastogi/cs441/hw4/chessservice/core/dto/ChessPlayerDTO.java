package com.mayankrastogi.cs441.hw4.chessservice.core.dto;

import com.mayankrastogi.cs441.hw4.chessservice.core.PlayerColor;

/**
 * Describes the response from the {@link com.mayankrastogi.cs441.hw4.chessservice.controllers.PlayerController}.
 */
public class ChessPlayerDTO {
    /**
     * Name of the client player.
     */
    public String playerName;
    /**
     * Color of the client player.
     */
    public PlayerColor playerColor;
    /**
     * Level of AI of the client player
     */
    public int playerAILevel;
    /**
     * Name of the server player.
     */
    public String serverPlayerName;
    /**
     * Color of the server player.
     */
    public PlayerColor serverPlayerColor;
    /**
     * Level of AI of the server player.
     */
    public int serverPlayerAILevel;
    /**
     * URL of the server against which the game is being played.
     */
    public String serverURL;
    /**
     * Outcome of the gameplay between the two players.
     */
    public ViewStateDTO outcome;
}
