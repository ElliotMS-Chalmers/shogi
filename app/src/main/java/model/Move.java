package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

import model.pieces.Piece;
import util.Pos;


public record Move(Pos from, Pos to, Piece movedPiece, Piece capturedPiece){

    @JsonCreator
    public Move(
            @JsonProperty("from") Pos from,
            @JsonProperty("to") Pos to,
            @JsonProperty("movedPiece") Piece movedPiece,
            @JsonProperty("capturedPiece") Piece capturedPiece)
    {
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
    }

    public boolean fromPlayerHand(){
        return (this.from() == null);
    }

    public String toString(){
        char moveType;

        if (from == null)
            moveType = '*';
        else if (capturedPiece != null)
            moveType = 'x';
        else
            moveType = '-';

        return movedPiece.getSfenAbbreviation()+moveType+(to.col()+1)+(to.row()+1);
    }
}