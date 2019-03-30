package com.mayankrastogi.cs441.hw4.chessservice.core;

/**
 * Describes a move for a chess game.
 */
public class ChessMove {

    /**
     * The algebraic notation of the square where the piece is currently located.
     * <p>
     * Example: "e1", where 'e' denotes the file (x position) labelled from 'a' to 'h'; '1' denotes the rank
     * (y position) labelled from 1 to 8.
     */
    public String fromSquare;
    /**
     * The algebraic notation of the square where the specified piece is to be moved.
     * <p>
     * Example: "e1", where 'e' denotes the file (x position) labelled from 'a' to 'h'; '1' denotes the rank
     * (y position) labelled from 1 to 8.
     */
    public String toSquare;
    /**
     * The name of the piece that a pawn should be promoted to if a pawn reaches the opponent's end of the chess board
     * as a result of the current move.
     */
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
