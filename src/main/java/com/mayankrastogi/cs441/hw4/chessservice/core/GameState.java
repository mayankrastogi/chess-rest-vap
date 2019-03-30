package com.mayankrastogi.cs441.hw4.chessservice.core;

/**
 * Valid states that the chess engine can be in.
 */
public enum GameState {
    WAITING_FOR_OPPONENT,
    THINKING,
    CHECKMATE,
    DRAW
}
