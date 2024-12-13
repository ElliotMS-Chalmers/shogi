package model.variants;

import model.Board;
import model.pieces.Piece;
import util.Pos;

public abstract class RuleSet {
    public abstract boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Variant variant);
}
