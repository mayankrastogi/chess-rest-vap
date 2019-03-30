package com.mayankrastogi.cs441.hw4.chessservice.core.exceptions;

/**
 * Indicates that an operation could not be performed since the chess game already was over.
 */
public class GameEndedException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "The chess game is over and thus no more moves can be made.";

    public GameEndedException() {
        super(DEFAULT_MESSAGE);
    }

    public GameEndedException(String message) {
        super(message);
    }

    public GameEndedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameEndedException(Throwable cause) {
        super(cause);
    }

    public GameEndedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
