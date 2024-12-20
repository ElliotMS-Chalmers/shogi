package model.variants;

import model.Board;
import model.Player;
import model.pieces.Piece;
import util.Pos;
import util.Side;

public interface RuleSet {
    boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Side side, Side oppositeSide);

    boolean isCurrentlyInCheck(Board board, Pos kingPos, Side side);

    boolean isCurrentlyInCheckMate(Board board, Pos kingPos, Side side, Side oppositeSide, Player player);

    boolean validHandMove(Pos pos, Class pieceClass, Board board, Side side);
}