package com.mayankrastogi.cs441.hw4.chessservice.core.exceptions;

/**
 * Indicates that a move requested to be made is invalid for the current state of the game.
 */
public class InvalidMoveException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "The specified move is not a valid move.";

    public InvalidMoveException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMoveException(Throwable cause) {
        super(cause);
    }

    public InvalidMoveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
