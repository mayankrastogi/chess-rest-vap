package com.mayankrastogi.cs441.hw4.chessservice.engines;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;
import com.mayankrastogi.cs441.hw4.chessservice.core.GameState;
import com.mayankrastogi.cs441.hw4.chessservice.core.PlayerColor;
import com.mayankrastogi.cs441.hw4.chessservice.core.TransferFormat;
import com.mayankrastogi.cs441.hw4.chessservice.core.exceptions.GameEndedException;
import com.mayankrastogi.cs441.hw4.chessservice.core.exceptions.GameLoadException;
import com.mayankrastogi.cs441.hw4.chessservice.core.exceptions.InvalidMoveException;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Defines an interface for making a chess engine.
 */
public interface ChessEngine {

    /**
     * Constant for identifying the AI player of the chess game.
     */
    String AI_PLAYER_NAME = "Chess AI";

    /**
     * Manages a mapping of game sessions registered by any chess engine.
     * <p>
     * The key denotes the game ID and the value is the instance of a chess engine.
     */
    Map<String, ChessEngine> GAME_SESSIONS = new HashMap<>();

    /**
     * Fetches an instance ChessEngine with the supplied gameID.
     *
     * @param gameID The ID of the ChessEngine instance.
     * @return The ChessEngine with the supplied gameID.
     */
    static ChessEngine getInstance(String gameID) {
        return GAME_SESSIONS.getOrDefault(gameID, null);
    }

    /**
     * Get ID of the ChessEngine instance.
     *
     * @return Game ID.
     */
    String getGameID();

    /**
     * Creates a new instance of ChessEngine for starting a new game.
     * <p>
     * The new instance should be added to the {@link #GAME_SESSIONS} mapping. Only a new instance of the game should be
     * created and no first-move should be made.
     *
     * @param playerName  Name of the human player.
     * @param playerColor Color of pieces the human player wants to play with.
     * @param aiLevel     Level of the AI player.
     * @return A new ChessEngine instance with the default board state.
     */
    ChessEngine newGame(String playerName, PlayerColor playerColor, int aiLevel);

    /**
     * Loads a chess game into the current instance from a string in the specified {@link TransferFormat}.
     *
     * @param data   The data describing a game state.
     * @param format The format of the data.
     * @throws GameLoadException If the data could not be loaded using the supplied data and format.
     */
    void loadGame(String data, TransferFormat format) throws GameLoadException;

    /**
     * Exports the current state of the game in the specified format.
     *
     * @param format The format in which the game state should be exported.
     * @return Data string that describes the game state in the specified format.
     */
    String exportGame(TransferFormat format);

    /**
     * Asks the AI to think of the next move based on the current state.
     *
     * @return A move that the AI thinks is the best one to make next.
     * @throws GameEndedException If the game has already ended.
     */
    ChessMove getNextMove() throws GameEndedException;

    /**
     * Makes the supplied move on the current game state.
     * <p>
     * If the next move is to be made by the AI, that move is made in the same call to this method.
     *
     * @param move The move to be made.
     * @throws InvalidMoveException If the move is not valid or the game has ended.
     */
    void makeMove(ChessMove move) throws InvalidMoveException;

    /**
     * Tells the color of the player who needs to make the next move.
     *
     * @return Color of the active player.
     */
    PlayerColor getActivePlayer();

    /**
     * Sets who needs to make the next move.
     *
     * @param playerColor Color of the new active player.
     */
    void setActivePlayer(PlayerColor playerColor);

    /**
     * Determines whether the current game has ended due to any reason.
     *
     * @return Whether the game has ended or not.
     */
    boolean hasGameEnded();

    /**
     * Gets a stack of all the moves made till now by either player.
     *
     * @return A stack of all the moves made till now by either player.
     */
    Stack<ChessMove> getMoveHistory();

    /**
     * Gets the state of the current game.
     *
     * @return Game state.
     */
    GameState getState();

    /**
     * Gets the name of the winning player if a winner has been determined.
     *
     * @return Name of the winner, `null` if the winner hasn't been determined yet.
     */
    String getWinner();
}
