package com.mayankrastogi.cs441.hw4.chessservice.core.dto;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;
import com.mayankrastogi.cs441.hw4.chessservice.core.GameState;

import java.util.ArrayList;
import java.util.List;

public class ViewStateDTO {
    public boolean hasGameEnded;
    public GameState status;
    public String fen;
    public String pgn;
    public String winner;
    public List<ChessMove> moves = new ArrayList<>();

    @Override
    public String toString() {
        return String.format(
                "[hasGameEnded: %s, status: %s, fen: %s, pgn: %s, winner: %s]",
                hasGameEnded, status, fen, pgn, winner);
    }
}
