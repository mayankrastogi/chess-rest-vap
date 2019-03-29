package com.mayankrastogi.cs441.hw4.chessservice.core.dto;

import com.mayankrastogi.cs441.hw4.chessservice.core.ChessMove;

public class MakeMoveDTO extends ErrorDTO {
    public ChessMove clientMove;
    public ChessMove serverMove;
}
