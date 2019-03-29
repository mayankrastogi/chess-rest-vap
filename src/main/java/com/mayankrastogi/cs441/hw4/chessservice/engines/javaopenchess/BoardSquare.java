package com.mayankrastogi.cs441.hw4.chessservice.engines.javaopenchess;

import pl.art.lach.mateusz.javaopenchess.core.Squares;

public class BoardSquare {

    public Squares x;
    public Squares y;

    public BoardSquare(Squares x, Squares y) {
        this.x = x;
        this.y = y;
    }

    public BoardSquare(String squareNotation) {
        StringBuilder strX = new StringBuilder("SQ_");
        StringBuilder strY = new StringBuilder("SQ_");

        strX.append(squareNotation.toUpperCase().charAt(0));
        strY.append(squareNotation.charAt(1));

        x = Squares.valueOf(strX.toString());
        y = Squares.valueOf(strY.toString());
    }

    @Override
    public String toString() {
        return x.toString().substring(3) + y.toString().substring(3);
    }
}
