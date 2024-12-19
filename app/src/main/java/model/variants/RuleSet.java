package model.variants;

import model.Board;
import model.pieces.Piece;
import util.Pos;
import util.Side;

public abstract class RuleSet {
    public abstract boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Side side, Side oppositeSide);

    public abstract boolean isCurrentlyInCheck(Board board, Pos kingPos, Side side);
    public abstract boolean isCurrentlyInCheckMate(Board board, Pos kingPos, Side oppositeSide);
}
