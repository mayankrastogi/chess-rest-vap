package com.mayankrastogi.cs441.hw4.chessservice.utils;

import com.mayankrastogi.cs441.hw4.chessservice.core.TransferFormat;
import com.mayankrastogi.cs441.hw4.chessservice.core.dto.ViewStateDTO;
import com.mayankrastogi.cs441.hw4.chessservice.engines.ChessEngine;

/**
 * Utility methods for use with the Chess Rest service.
 */
public class ChessUtils {

    /**
     * Serializes the response for the current game state.
     *
     * @param gameID The ID of the game whose state is to be serialized.
     * @return The serialized state of the game.
     */
    public static ViewStateDTO getGameState(String gameID) {
        ChessEngine chessEngine = ChessEngine.getInstance(gameID);

        ViewStateDTO state = new ViewStateDTO();
        state.hasGameEnded = chessEngine.hasGameEnded();
        state.fen = chessEngine.exportGame(TransferFormat.FEN);
        state.pgn = chessEngine.exportGame(TransferFormat.PGN);
        state.status = chessEngine.getState();
        state.winner = chessEngine.getWinner();
        state.moves = chessEngine.getMoveHistory();

        return state;
    }
}
