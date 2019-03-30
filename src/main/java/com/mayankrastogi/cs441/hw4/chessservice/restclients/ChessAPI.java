package com.mayankrastogi.cs441.hw4.chessservice.restclients;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;
import com.mayankrastogi.cs441.hw4.chessservice.core.PlayerColor;
import com.mayankrastogi.cs441.hw4.chessservice.core.dto.ChessMoveDTO;
import com.mayankrastogi.cs441.hw4.chessservice.core.dto.ViewStateDTO;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Describes the Chess REST API for generating a REST client for that service using {@link retrofit2.Retrofit}.
 */
public interface ChessAPI {

    /**
     * Starts a new chess game and returns the initial state.
     * <p>
     * If the player chooses BLACK color, the first-move is made by the AI and included in the response.
     *
     * @param playerName      Your name.
     * @param playerColor     The color you wish to play with (BLACK | WHITE).
     * @param opponentAILevel Level of AI you wish to play against (1 | 2).
     * @return Response of the API call containing the initial state of the new game along with the gameID.
     */
    @FormUrlEncoded
    @POST("chess/new")
    Call<ChessMoveDTO> newGame(
            @Field("playerName") String playerName,
            @Field("playerColor") PlayerColor playerColor,
            @Field("opponentAILevel") int opponentAILevel);

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
     * @return Response of the API call containing the new state of the game along with the AI opponent's move.
     */
    @PUT("chess/{id}/move")
    Call<ChessMoveDTO> makeMove(
            @Path("id") String gameID,
            @Body ChessMove move);

    /**
     * Retrieves the current state of the supplied chess game.
     *
     * @param gameID The ID of the chess game whose state is to be fetched.
     * @return Response of the API call containing the game state.
     */
    @GET("chess/{id}/state")
    Call<ViewStateDTO> getState(@Path("id") String gameID);
}
