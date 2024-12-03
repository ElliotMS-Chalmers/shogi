package model;

import model.pieces.Piece;
import util.Pos;

public record Move(Pos from, Pos to, Piece movedPiece, Piece capturedPiece){
    public boolean fromPlayerHand(){
        return (this.from() == null);
    }
}