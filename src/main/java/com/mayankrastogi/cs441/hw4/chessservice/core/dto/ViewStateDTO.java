package com.mayankrastogi.cs441.hw4.chessservice.core.dto;

import com.mayankrastogi.cs441.hw4.chessservice.core.GameState;

public class ViewStateDTO extends ErrorDTO {
    public boolean hasGameEnded;
    public String fen;
    public String pgn;
    public GameState status;
    public String winner;
}
