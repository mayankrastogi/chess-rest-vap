package com.mayankrastogi.cs441.hw4.chessservice.restclients;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;
import com.mayankrastogi.cs441.hw4.chessservice.core.PlayerColor;
import com.mayankrastogi.cs441.hw4.chessservice.core.dto.ChessMoveDTO;
import com.mayankrastogi.cs441.hw4.chessservice.core.dto.ViewStateDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface ChessAPI {

    @FormUrlEncoded
    @POST("chess/new")
    Call<ChessMoveDTO> newGame(
            @Field("playerName") String playerName,
            @Field("playerColor") PlayerColor playerColor,
            @Field("opponentAILevel") int opponentAILevel);

    @PUT("chess/{id}/move")
    Call<ChessMoveDTO> makeMove(
            @Path("id") String gameID,
            @Body ChessMove move);

    @GET("chess/{id}/state")
    Call<ViewStateDTO> getState(@Path("id") String gameID);
}
