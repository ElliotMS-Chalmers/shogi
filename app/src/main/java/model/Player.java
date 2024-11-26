package model;

import pieces.Piece;

public class Player {
    private boolean team;

    public Player(Piece[] pieces, Piece[] capturedPieces, boolean team){
        this.team = team;
    }


}
