package model.variants;

import model.game.Board;
import model.game.Player;
import model.pieces.Piece;
import util.Pos;
import util.Side;

/**
 * Interface for rule sets that define the behavior of different Shogi game variants.
 */
public interface RuleSet {

    /**
     * Validates whether a move is legal for a given piece.
     *
     * @param posFrom the starting position of the piece.
     * @param posTo the target position for the move.
     * @param piece the piece being moved.
     * @param board the current state of the game board.
     * @param side the side of the player making the move.
     * @param oppositeSide the side of the opponent.
     * @return true if the move is valid, false otherwise.
     */
    public abstract boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Side side, Side oppositeSide);

    /**
     * Checks whether the current player’s king is in check.
     *
     * @param board the current state of the game board.
     * @param kingPos the position of the player's king on the board.
     * @param side the side of the player whose king's status is being checked.
     * @return true if the king is in check, false otherwise.
     */
    public abstract boolean isCurrentlyInCheck(Board board, Pos kingPos, Side side);

    /**
     * Checks whether the current player’s king is in checkmate.
     *
     * @param board the current state of the game board.
     * @param kingPos the position of the player's king.
     * @param side the side of the player whose king's status is being checked.
     * @param oppositeSide the side of the opponent.
     * @param player the current player.
     * @return true if the king is in checkmate, false otherwise.
     */
    public abstract boolean isCurrentlyInCheckMate(Board board, Pos kingPos, Side side, Side oppositeSide, Player player);

    /**
     * Validates a move from a player's hand.
     *
     * @param pos the target position for placing a piece from the hand.
     * @param pieceClass the class of the piece being placed from the hand.
     * @param board the current state of the game board.
     * @param side the side of the player making the move.
     * @return true if the hand move is valid, false otherwise.
     */
    public abstract boolean validHandMove(Pos pos, Class pieceClass, Board board, Side side);
}