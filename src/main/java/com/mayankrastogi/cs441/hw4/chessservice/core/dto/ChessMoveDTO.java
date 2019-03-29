package com.mayankrastogi.cs441.hw4.chessservice.core.dto;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;

public class ChessMoveDTO {
    public String gameID;
    public ChessMove clientMove;
    public ChessMove serverMove;
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
