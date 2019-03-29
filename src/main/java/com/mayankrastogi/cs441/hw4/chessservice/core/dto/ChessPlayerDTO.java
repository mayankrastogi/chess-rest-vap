package com.mayankrastogi.cs441.hw4.chessservice.core.dto;

import com.mayankrastogi.cs441.hw4.chessservice.core.PlayerColor;

public class ChessPlayerDTO {
    public String playerName;
    public PlayerColor playerColor;
    public int playerAILevel;
    public String serverPlayerName;
    public PlayerColor serverPlayerColor;
    public int serverPlayerAILevel;
    public String serverURL;
    public ViewStateDTO outcome;
}
