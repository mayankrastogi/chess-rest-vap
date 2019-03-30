package com.mayankrastogi.cs441.hw4.chessservice.core.dto;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;
import com.mayankrastogi.cs441.hw4.chessservice.core.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the state of a chess game.
 */
public class ViewStateDTO {
    /**
     * Whether this game has ended due to any reason.
     */
    public boolean hasGameEnded;
    /**
     * The current status of the game engine.
     */
    public GameState status;
    /**
     * Game state exported in FEN notation.
     */
    public String fen;
    /**
     * Game state exported in PQN notation.
     */
    public String pgn;
    /**
     * Name of the winner if the winner has been determined.
     */
    public String winner;
    /**
     * History of all the moves made by either player.
     */
    public List<ChessMove> moves = new ArrayList<>();

    @Override
    public String toString() {
        return String.format(
                "[hasGameEnded: %s, status: %s, fen: %s, pgn: %s, winner: %s]",
                hasGameEnded, status, fen, pgn, winner);
    }
}
