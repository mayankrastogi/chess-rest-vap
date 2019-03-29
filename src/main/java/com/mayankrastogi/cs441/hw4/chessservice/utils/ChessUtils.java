package com.mayankrastogi.cs441.hw4.chessservice.utils;

import com.mayankrastogi.cs441.hw4.chessservice.core.TransferFormat;
import com.mayankrastogi.cs441.hw4.chessservice.core.dto.ViewStateDTO;
import com.mayankrastogi.cs441.hw4.chessservice.engines.ChessEngine;

public class ChessUtils {

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
