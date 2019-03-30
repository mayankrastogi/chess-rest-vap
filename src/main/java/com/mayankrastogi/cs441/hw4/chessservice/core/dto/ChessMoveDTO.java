package com.mayankrastogi.cs441.hw4.chessservice.core.dto;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;

/**
 * Describes the response from the Chess REST service.
 */
public class ChessMoveDTO {
    /**
     * ID of the new/current game.
     */
    public String gameID;
    /**
     * The last move made by the client player.
     */
    public ChessMove clientMove;
    /**
     * The last move made by the server player.
     */
    public ChessMove serverMove;
    /**
     * The current state of the game.
     */
    public ViewStateDTO status;

    @Override
    public String toString() {
        return String.format(
                "[gameID: %s, clientMove: %s, serverMove: %s, fen: %s]",
                gameID,
                clientMove,
                serverMove,
                status);
    }
}
