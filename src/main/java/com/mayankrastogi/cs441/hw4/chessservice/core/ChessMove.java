package com.mayankrastogi.cs441.hw4.chessservice.core;

public class ChessMove {

    public String fromSquare;
    public String toSquare;
    public ChessPiece promotionPiece;

    public ChessMove() {
    }

    public ChessMove(String fromSquare, String toSquare, ChessPiece promotionPiece) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.promotionPiece = promotionPiece;
    }

    @Override
    public String toString() {
        return String.format(
                "[fromSquare: %1$s, toSquare: %2$s, promotionPiece: %3$s]",
                fromSquare,
                toSquare,
                promotionPiece);
    }
}
