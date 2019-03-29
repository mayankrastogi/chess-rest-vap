package com.mayankrastogi.cs441.hw4.chessservice.engines.javaopenchess;

import pl.art.lach.mateusz.javaopenchess.core.Chessboard;
import pl.art.lach.mateusz.javaopenchess.core.Colors;
import pl.art.lach.mateusz.javaopenchess.core.pieces.Piece;
import pl.art.lach.mateusz.javaopenchess.core.pieces.PieceFactory;
import pl.art.lach.mateusz.javaopenchess.core.players.PlayerType;
import pl.art.lach.mateusz.javaopenchess.core.players.implementation.ComputerPlayer;

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
