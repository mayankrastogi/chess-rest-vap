package com.mayankrastogi.cs441.hw4.chessservice.engines.javaopenchess;

import pl.art.lach.mateusz.javaopenchess.core.Chessboard;
import pl.art.lach.mateusz.javaopenchess.core.Colors;
import pl.art.lach.mateusz.javaopenchess.core.pieces.Piece;
import pl.art.lach.mateusz.javaopenchess.core.pieces.PieceFactory;
import pl.art.lach.mateusz.javaopenchess.core.players.PlayerType;
import pl.art.lach.mateusz.javaopenchess.core.players.implementation.ComputerPlayer;

/**
 * A {@link pl.art.lach.mateusz.javaopenchess.core.players.Player} that returns a valid piece when asked for
 * {@link #getPromotionPiece(Chessboard)}.
 * <p>
 * Both {@link pl.art.lach.mateusz.javaopenchess.core.players.implementation.HumanPlayer} and {@link ComputerPlayer} are
 * not suitable for use since the former prompts the user with a dialog box for selecting a promotion piece and the
 * latter always returns `null` resulting in a {@link NullPointerException}.
 */
public class ChessPlayer extends ComputerPlayer {

    public ChessPlayer() {
        super();
    }

    public ChessPlayer(String name, String color) {
        super(name, color);
    }

    public ChessPlayer(String name, Colors color) {
        super(name, color);
    }

    public ChessPlayer(String name, Colors color, PlayerType playerType) {
        super(name, color);
        this.playerType = playerType;
    }

    @Override
    public Piece getPromotionPiece(Chessboard chessboard) {
        return PieceFactory.getPiece(chessboard, getColor(), "Queen", this);
    }
}
