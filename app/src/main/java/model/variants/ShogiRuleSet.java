package model.variants;

import model.Board;
import model.pieces.King;
import model.pieces.Piece;
import util.Pos;


import java.util.ArrayList;


public class ShogiRuleSet extends RuleSet{

    @Override
    public boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Variant variant){
        if (piece instanceof King){
            return false;
        }
        return (piece.getAvailableMoves(posFrom, board, variant).contains(posTo));
    }


}
