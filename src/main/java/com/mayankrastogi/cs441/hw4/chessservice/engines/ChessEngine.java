package com.mayankrastogi.cs441.hw4.chessservice.engines;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;
import com.mayankrastogi.cs441.hw4.chessservice.core.GameState;
import com.mayankrastogi.cs441.hw4.chessservice.core.PlayerColor;
import com.mayankrastogi.cs441.hw4.chessservice.core.TransferFormat;
import com.mayankrastogi.cs441.hw4.chessservice.core.exceptions.GameEndedException;
import com.mayankrastogi.cs441.hw4.chessservice.core.exceptions.GameLoadException;
import com.mayankrastogi.cs441.hw4.chessservice.core.exceptions.InvalidMoveException;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public interface ChessEngine {

    Map<String, ChessEngine> GAME_SESSIONS = new HashMap<>();

    static ChessEngine getInstance(String gameID) {
        return GAME_SESSIONS.getOrDefault(gameID, null);
    }

    String getGameID();

    ChessEngine newGame(String playerName, PlayerColor playerColor, int aiLevel);

    void loadGame(String data, TransferFormat format) throws GameLoadException;

    String exportGame(TransferFormat format);

    ChessMove getNextMove() throws GameEndedException;

    void makeMove(ChessMove move) throws InvalidMoveException;

    PlayerColor getActivePlayer();

    void setActivePlayer(PlayerColor playerColor);

    boolean hasGameEnded();

    Stack<ChessMove> getMoveHistory();

    GameState getState();

    String getWinner();
}
