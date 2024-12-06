package model;

import model.pieces.King;
import model.pieces.Piece;
import util.Pos;

import java.util.ArrayList;


public class ShogiRuleSet {

    public boolean validMove(Pos posFrom, Pos posTo, Piece piece){
        if (piece instanceof King){
            return false;
        }
        return (piece.getAvailableMoves(posFrom).contains(posTo));
    }
}
