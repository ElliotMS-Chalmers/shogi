package util;

import model.pieces.Piece;

public record Move(Pos from, Pos to, Piece movedPiece, Piece capturedPiece){
    public boolean fromPlayerHand(){
        return (this.from() == null);
    }
}