package model.variants;

import model.Board;
import model.Player;
import model.pieces.Piece;
import util.Pos;
import util.Side;

public interface RuleSet {
    public abstract boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Side side, Side oppositeSide);
    public abstract boolean isCurrentlyInCheck(Board board, Pos kingPos, Side side);
    public abstract boolean isCurrentlyInCheckMate(Board board, Pos kingPos, Side side, Side oppositeSide, Player player);
    public abstract boolean validHandMove(Pos pos, Class pieceClass, Board board, Side side);
}