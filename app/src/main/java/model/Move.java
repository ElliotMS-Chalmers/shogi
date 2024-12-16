package model;

import model.pieces.Piece;
import util.Pos;

public record Move(Pos from, Pos to, Piece movedPiece, Piece capturedPiece){
    public boolean fromPlayerHand(){
        return (this.from() == null);
    }

    public String toString(){
        char moveType;
        if(from == null){moveType = '*';}
        else if(capturedPiece != null){moveType = 'x';}
        else{moveType = '-';}
        return movedPiece.getSfenAbbreviation()+moveType+(to.col()+1)+(to.row()+1);
    }
}