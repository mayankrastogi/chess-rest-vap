package com.mayankrastogi.cs441.hw4.chessservice.engines.javaopenchess;

import com.mayankrastogi.cs441.hw4.chessservice.core.*;
import com.mayankrastogi.cs441.hw4.chessservice.core.exceptions.GameEndedException;
import com.mayankrastogi.cs441.hw4.chessservice.core.exceptions.GameLoadException;
import com.mayankrastogi.cs441.hw4.chessservice.core.exceptions.InvalidMoveException;
import com.mayankrastogi.cs441.hw4.chessservice.engines.ChessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.art.lach.mateusz.javaopenchess.core.Colors;
import pl.art.lach.mateusz.javaopenchess.core.Game;
import pl.art.lach.mateusz.javaopenchess.core.Square;
import pl.art.lach.mateusz.javaopenchess.core.ai.AIFactory;
import pl.art.lach.mateusz.javaopenchess.core.data_transfer.DataExporter;
import pl.art.lach.mateusz.javaopenchess.core.data_transfer.DataImporter;
import pl.art.lach.mateusz.javaopenchess.core.data_transfer.implementations.FenNotation;
import pl.art.lach.mateusz.javaopenchess.core.data_transfer.implementations.PGNNotation;
import pl.art.lach.mateusz.javaopenchess.core.exceptions.ReadGameError;
import pl.art.lach.mateusz.javaopenchess.core.moves.Move;
import pl.art.lach.mateusz.javaopenchess.core.pieces.implementation.King;
import pl.art.lach.mateusz.javaopenchess.core.players.Player;
import pl.art.lach.mateusz.javaopenchess.core.players.PlayerType;
import pl.art.lach.mateusz.javaopenchess.utils.GameModes;
import pl.art.lach.mateusz.javaopenchess.utils.GameTypes;
import pl.art.lach.mateusz.javaopenchess.utils.Settings;

import java.util.Stack;
import java.util.UUID;

@RequestScope
@Service
public class JavaOpenChessEngine implements ChessEngine {

    private static final String SERVER_PLAYER_NAME = "Chess AI";
    private static final Logger LOG = LoggerFactory.getLogger(JavaOpenChessEngine.class);

    private Game game;
    private UUID id;
    private String winner;
    private GameState state;

    @Autowired
    public JavaOpenChessEngine() {
        game = null;
        id = UUID.randomUUID();
        winner = null;
        state = GameState.WAITING_FOR_OPPONENT;
    }

    public JavaOpenChessEngine(String playerName, PlayerColor playerColor, int aiLevel) {
        this();

        String whitePlayerName, blackPlayerName;
        PlayerType whitePlayerType, blackPlayerType;
        Player whitePlayer, blackPlayer;

        if (playerColor == PlayerColor.WHITE) {
            whitePlayerName = playerName;
            whitePlayerType = PlayerType.LOCAL_USER;

            blackPlayerName = SERVER_PLAYER_NAME;
            blackPlayerType = PlayerType.COMPUTER;
        } else {
            blackPlayerName = playerName;
            blackPlayerType = PlayerType.LOCAL_USER;

            whitePlayerName = SERVER_PLAYER_NAME;
            whitePlayerType = PlayerType.COMPUTER;
        }

        whitePlayer = new ChessPlayer(whitePlayerName, Colors.WHITE, whitePlayerType);
        blackPlayer = new ChessPlayer(blackPlayerName, Colors.BLACK, blackPlayerType);

        Settings settings = new Settings(whitePlayer, blackPlayer);
        settings.setGameMode(GameModes.NEW_GAME);
        settings.setGameType(GameTypes.LOCAL);

        game = new Game();
        game.setSettings(settings);
        game.getChessboard().setPieces4NewGame(whitePlayer, blackPlayer);
        game.setActivePlayer(whitePlayer);
        game.setAi(AIFactory.getAI(aiLevel));

        GAME_SESSIONS.put(getGameID(), this);
    }

    @Override
    public String getGameID() {
        return id.toString();
    }

    @Override
    public ChessEngine newGame(String playerName, PlayerColor playerColor, int aiLevel) {
        return new JavaOpenChessEngine(playerName, playerColor, aiLevel);
    }

    @Override
    public void loadGame(String data, TransferFormat format) throws GameLoadException {
        try {
            DataImporter importer = format == TransferFormat.FEN ? new FenNotation() : new PGNNotation();
            game = importer.importData(data);
            game.setAi(AIFactory.getAI(2));
        } catch (ReadGameError e) {
            LOG.error("Error loading game: ", e);
            throw new GameLoadException(GameLoadException.DEFAULT_MESSAGE, e);
        }
    }

    @Override
    public String exportGame(TransferFormat format) {
        DataExporter exporter = format == TransferFormat.FEN ? new FenNotation() : new PGNNotation();
        return exporter.exportData(game);
    }

    @Override
    public ChessMove getNextMove() throws GameEndedException {
        Move computedMove = game.getAi().getMove(game, game.getMoves().getLastMoveFromHistory());
        return getChessMoveFromMove(computedMove);
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        state = GameState.THINKING;

        BoardSquare fromSquare = new BoardSquare(move.fromSquare);
        BoardSquare toSquare = new BoardSquare(move.toSquare);

        selectSquare(game.getChessboard().getSquare(fromSquare.x, fromSquare.y));

        Square sq = game.getChessboard().getSquare(toSquare.x, toSquare.y);

        if (canInvokeMoveAction(sq)) //move
        {
            game.getChessboard().move(game.getChessboard().getActiveSquare(), sq);
            game.getChessboard().unselect();

            //switch player
            game.nextMove();

            testForEndOfGame();

            if (canDoComputerMove()) {
                game.doComputerMove();
                testForEndOfGame();
            }
        } else {
            state = GameState.WAITING_FOR_OPPONENT;
            throw new InvalidMoveException(
                    move.toString() + " is not a valid move; game state = \"" + exportGame(TransferFormat.FEN) + "\"");
        }
        state = GameState.WAITING_FOR_OPPONENT;
    }

    @Override
    public PlayerColor getActivePlayer() {
        return game.getActivePlayer().getColor() == Colors.WHITE ? PlayerColor.WHITE : PlayerColor.BLACK;
    }

    @Override
    public void setActivePlayer(PlayerColor playerColor) {
        if (game != null) {
            switch (playerColor) {
                case WHITE:
                    game.setActivePlayer(game.getSettings().getPlayerWhite());
                    break;
                case BLACK:
                    game.setActivePlayer(game.getSettings().getPlayerBlack());
                    break;
            }
        }
    }

    @Override
    public boolean hasGameEnded() {
        return game == null || game.isIsEndOfGame();
    }

    @Override
    public Stack<ChessMove> getMoveHistory() {
        Stack<ChessMove> moveStack = new Stack<>();

        if (game != null) {
            for (Move move : game.getMoves().getMoveBackStack()) {
                moveStack.push(getChessMoveFromMove(move));
            }
        }

        return moveStack;
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public String getWinner() {
        return winner;
    }

    private void selectSquare(Square sq) {
        game.getChessboard().unselect();
        game.getChessboard().select(sq);
    }

    private boolean canInvokeMoveAction(Square sq) {
        return game.getChessboard().getActiveSquare() != null
                && game.getChessboard().getActiveSquare().piece != null
                && game.getChessboard().getActiveSquare().getPiece().getAllMoves().contains(sq);
    }

    private boolean canDoComputerMove() {
        return !game.isIsEndOfGame()
                && game.getSettings().isGameAgainstComputer()
                && game.getActivePlayer().getPlayerType() == PlayerType.COMPUTER
                && game.getAi() != null;
    }

    private void testForEndOfGame() {
        //checkmate or stalemate
        King activeKing;
        King opponentKing;
        if (game.getActivePlayer() == game.getSettings().getPlayerWhite()) {
            activeKing = game.getChessboard().getKingWhite();
            opponentKing = game.getChessboard().getKingBlack();
        } else {
            activeKing = game.getChessboard().getKingBlack();
            opponentKing = game.getChessboard().getKingWhite();
        }

        switch (activeKing.isCheckmatedOrStalemated()) {
            case 1:
                game.setIsEndOfGame(true);
                state = GameState.CHECKMATE;
                winner = opponentKing.getPlayer().getName();

                break;
            case 2:
                game.setIsEndOfGame(true);
                state = GameState.DRAW;
                break;
        }
    }

    private ChessMove getChessMoveFromMove(Move move) {
        if (move == null) return null;

        ChessPiece promotionPiece = null;
        if (move.getPromotedPiece() != null) {
            promotionPiece = ChessPiece.valueOf(move.getPromotedPiece().getName().toUpperCase());
        }
        return new ChessMove(
                move.getFrom().getAlgebraicNotation(),
                move.getTo().getAlgebraicNotation(),
                promotionPiece
        );
    }
}
