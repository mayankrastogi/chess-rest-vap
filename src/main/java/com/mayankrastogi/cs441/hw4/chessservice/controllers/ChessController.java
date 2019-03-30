package com.mayankrastogi.cs441.hw4.chessservice.controllers;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;
import com.mayankrastogi.cs441.hw4.chessservice.core.PlayerColor;
import com.mayankrastogi.cs441.hw4.chessservice.core.TransferFormat;
import com.mayankrastogi.cs441.hw4.chessservice.core.dto.ChessMoveDTO;
import com.mayankrastogi.cs441.hw4.chessservice.core.dto.ViewStateDTO;
import com.mayankrastogi.cs441.hw4.chessservice.engines.ChessEngine;
import com.mayankrastogi.cs441.hw4.chessservice.utils.ChessUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.EmptyStackException;

/**
 * Exposes a RESTful web service to play chess against an AI opponent.
 */
@RestController
public class ChessController {

    private static final Logger LOG = LoggerFactory.getLogger(ChessController.class);

    private ChessEngine chessEngine;

    /**
     * Setter for injecting the chess engine.
     *
     * @param chessEngine The chess engine.
     */
    @Autowired
    public void setChessEngine(ChessEngine chessEngine) {
        this.chessEngine = chessEngine;
    }

    /**
     * Starts a new chess game and returns the initial state.
     * <p>
     * If the player chooses BLACK color, the first-move is made by the AI and included in the response.
     *
     * @param playerName      Your name.
     * @param playerColor     The color you wish to play with (BLACK | WHITE).
     * @param opponentAILevel Level of AI you wish to play against (1 | 2).
     * @return The initial state of the new game along with the gameID.
     */
    @PostMapping("/chess/new")
    public ChessMoveDTO newGame(
            @RequestParam String playerName,
            @RequestParam PlayerColor playerColor,
            @RequestParam int opponentAILevel) {

        LOG.trace(String.format(
                "newGame(playerName=%s, playerColor=%s, opponentAILevel=%s)",
                playerName, playerColor, opponentAILevel));

        return loadGame(null, playerName, playerColor, opponentAILevel);
    }

    /**
     * Loads a state of the game described in FEN notation.
     *
     * @param fen             The state of the game in FEN notation.
     * @param playerName      Your name.
     * @param playerColor     The color you wish to play with (BLACK | WHITE).
     * @param opponentAILevel Level of AI you wish to play against (1 | 2).
     * @return The state of the loaded game.
     */
    @PostMapping("/chess/load")
    public ChessMoveDTO loadGame(
            @RequestParam String fen,
            @RequestParam String playerName,
            @RequestParam PlayerColor playerColor,
            @RequestParam int opponentAILevel) {

        LOG.trace(String.format(
                "loadGame(fen=%s, playerName=%s, playerColor=%s, opponentAILevel=%s)",
                fen, playerName, playerColor, opponentAILevel));

        ChessMoveDTO response = new ChessMoveDTO();

        // Create a new game
        ChessEngine newGame = chessEngine.newGame(playerName, playerColor, opponentAILevel);

        // If fen param was specified, load this state
        if (fen != null) {
            newGame.loadGame(fen, TransferFormat.FEN);
        }

        // If the human player is BLACK, make the first move (since we are WHITE)
        if (playerColor == PlayerColor.BLACK) {
            newGame.makeMove(newGame.getNextMove());
        }

        // Build the response
        response.gameID = newGame.getGameID();
        response.clientMove = null;
        try {
            response.serverMove = newGame.getMoveHistory().peek();
        } catch (EmptyStackException e) {
            response.serverMove = null;
        }
        response.status = ChessUtils.getGameState(newGame.getGameID());

        return response;
    }

    /**
     * Makes the supplied move on the specified chess game.
     *
     * @param gameID The game on which the move is to be made.
     * @param move   Description of the move. Example:
     *               <code>
     *               {
     *                  "fromSquare": "e7",
     *                  "toSquare": "e8",
     *                  "promotionPiece": "QUEEN"
     *               }
     *               </code>
     *               The promotionPiece should be supplied when a move is made that results in a pawn reaching the
     *               opponent's end of the chess board.
     * @return The new state of the game along with the AI opponent's move.
     */
    @PutMapping("/chess/{gameID}/move")
    public ChessMoveDTO makeMove(
            @PathVariable String gameID,
            @RequestBody ChessMove move) {

        LOG.trace(String.format("makeMove(gameID=%s, move=%s)", gameID, move));

        // Load the specified game
        ChessEngine chessEngine = ChessEngine.getInstance(gameID);

        // Make the move supplied by the human player and then make the next move using the AI
        chessEngine.makeMove(move);

        // Build the response
        ChessMoveDTO response = new ChessMoveDTO();
        response.gameID = gameID;
        response.clientMove = move;
        response.serverMove = chessEngine.getMoveHistory().peek();
        response.status = ChessUtils.getGameState(gameID);

        return response;
    }

    /**
     * Retrieves the current state of the supplied chess game.
     *
     * @param gameID The ID of the chess game whose state is to be fetched.
     * @return The state.
     */
    @GetMapping("/chess/{gameID}/state")
    public ViewStateDTO getState(@PathVariable String gameID) {

        LOG.trace(String.format("viewState(gameID=%s)", gameID));
        return ChessUtils.getGameState(gameID);
    }
}
