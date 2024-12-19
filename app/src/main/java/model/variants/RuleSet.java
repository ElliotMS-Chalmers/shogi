package model.variants;

import model.Board;
import model.Player;
import model.pieces.Piece;
import util.Pos;
import util.Side;

public interface RuleSet {
    public abstract boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Variant variant, Side side, Side oppositeSide);
    public abstract boolean isCurrentlyInCheck(Board board, Variant variant, Pos kingPos, Side side);
    public abstract boolean isCurrentlyInCheckMate(Board board, Variant variant, Pos kingPos, Side side, Side oppositeSide, Player player);
    public abstract boolean validHandMove(Pos pos, Class pieceClass, Board board, Variant variant, Side side);
}
