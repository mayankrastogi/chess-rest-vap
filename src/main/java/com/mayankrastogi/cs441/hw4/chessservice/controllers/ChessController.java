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

@RestController
public class ChessController {

    private static final Logger LOG = LoggerFactory.getLogger(ChessController.class);

    private ChessEngine chessEngine;

    @Autowired
    public void setChessEngine(ChessEngine chessEngine) {
        this.chessEngine = chessEngine;
    }

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

        ChessEngine newGame = chessEngine.newGame(playerName, playerColor, opponentAILevel);
        if (fen != null) {
            newGame.loadGame(fen, TransferFormat.FEN);
        }
        if (playerColor == PlayerColor.BLACK) {
            newGame.makeMove(newGame.getNextMove());
        }

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

    @PutMapping("/chess/{gameID}/move")
    public ChessMoveDTO makeMove(
            @PathVariable String gameID,
            @RequestBody ChessMove move) {

        LOG.trace(String.format("makeMove(gameID=%s, move=%s)", gameID, move));

        ChessEngine chessEngine = ChessEngine.getInstance(gameID);

        chessEngine.makeMove(move);

        ChessMoveDTO response = new ChessMoveDTO();
        response.gameID = gameID;
        response.clientMove = move;
        response.serverMove = chessEngine.getMoveHistory().peek();
        response.status = ChessUtils.getGameState(gameID);

        return response;
    }

    @GetMapping("/chess/{gameID}/state")
    public ViewStateDTO getState(@PathVariable String gameID) {

        LOG.trace(String.format("viewState(gameID=%s)", gameID));
        return ChessUtils.getGameState(gameID);
    }
}
