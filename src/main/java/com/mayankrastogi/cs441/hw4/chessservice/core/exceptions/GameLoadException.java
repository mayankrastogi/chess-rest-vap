package com.mayankrastogi.cs441.hw4.chessservice.core.exceptions;

/**
 * Indicates that there was a problem while loading the game.
 */
public class GameLoadException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "The game could not be loaded using the specified input data.";

    public GameLoadException() {
        super(DEFAULT_MESSAGE);
    }

    public GameLoadException(String message) {
        super(message);
    }

    public GameLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameLoadException(Throwable cause) {
        super(cause);
    }

    public GameLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
