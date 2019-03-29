package com.mayankrastogi.cs441.hw4.chessservice.core;

public enum ChessPiece {
    BISHOP("bishop", "B"),
    KNIGHT("knight", "N"),
    ROOK("rook", "R"),
    QUEEN("queen", "Q"),
    KING("king", "K"),
    PAWN("pawn", "P")
    ;

    private String name;
    private String symbol;

    ChessPiece(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }
}
