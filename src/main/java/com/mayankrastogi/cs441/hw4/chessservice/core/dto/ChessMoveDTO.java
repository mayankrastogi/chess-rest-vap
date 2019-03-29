package com.mayankrastogi.cs441.hw4.chessservice.core.dto;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;

public class ChessMoveDTO extends ErrorDTO {
    public String gameID;
    public ChessMove clientMove;
    public ChessMove serverMove;
    public String fen;

    @Override
    public String toString() {
        return String.format(
                "[gameID: %1$s, clientMove: %2$s, serverMove: %3$s, fen: %4$s]",
                gameID,
                clientMove,
                serverMove,
                fen);
    }
}
