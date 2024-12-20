package model.pieces;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import model.game.Board;
import util.Side;
import util.Pos;

import java.util.ArrayList;

/**
 * Abstract class representing a piece.
 * This class serves as a base for all specific piece types.
 */
@JsonDeserialize(using = PieceDeserializer.class)
public abstract class Piece {
    /**
     * The side of the piece (Sente or Gote)
     */
    protected Side side;

    /**
     * Constructs a Piece for a given side (SENTE or GOTE).
     *
     * @param side the side of the player (SENTE or GOTE) controlling this piece.
     */
    public Piece(Side side) {
        this.side = side;
    }

    /**
     * Returns the side of the player (SENTE or GOTE) controlling this piece.
     *
     * @return the side (SENTE or GOTE) of the piece.
     */
    public Side getSide() {
        return side;
    }

    /**
     * Returns the Shogi SFEN (Shogi Forsyth-Edwards Notation) abbreviation for the piece.
     * The abbreviation is represented by the first letter of the class name (e.g., 'P' for Pawn).
     * The case of the letter is determined by the piece's side: uppercase for SENTE, lowercase for GOTE.
     *
     * @return the SFEN abbreviation for this piece.
     */
    @JsonValue
    public String getSfenAbbreviation(){
        char letter = this.getClass().getSimpleName().charAt(0);
        return switch(side){
            case SENTE -> String.valueOf(letter);
            case GOTE -> String.valueOf(letter).toLowerCase();
        };
    }

    /**
     * Returns a list of available moves for this piece, taking into account its position and the current state of the board.
     * This method must be implemented by subclasses to provide the specific movement rules for each piece type.
     *
     * @param pos the current position of the piece on the board.
     * @param board the board on which the piece is placed, used for checking the legality of moves.
     * @return a list of positions that the piece can legally move to.
     */
    public abstract ArrayList<Pos> getAvailableMoves(Pos pos, Board board);

    /**
     * Returns a list of available moves for this piece in the backend context, considering its position and the current state of the board.
     * This method is generally used for move generation before any additional filtering.
     * It must also be implemented by subclasses to provide the specific movement rules for each piece type.
     *
     * @param pos the current position of the piece on the board.
     * @param board the board on which the piece is placed, used for checking the legality of moves.
     * @return a list of positions that the piece can legally move to.
     */
    public abstract ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board);

    /**
     * Checks if a move is within the boundaries of the board.
     *
     * @param pos the position to check.
     * @param board the board on which the piece is placed.
     * @return true if the move is within bounds, false otherwise.
     */
    public boolean checkLegalMoveWithinBounds(Pos pos, Board board) {
        return pos.col() >= 0 && pos.col() <= (board.getWidth() - 1) && pos.row() >= 0 && pos.row() <= (board.getHeight() - 1);
    }

    /**
     * Checks if a move does not result in capturing the piece's own side.
     * If the target square contains a piece, it ensures the piece belongs to the opposite side.
     *
     * @param pos the position to check.
     * @param board the board on which the piece is placed.
     * @return true if the move does not capture the piece's own side, false otherwise.
     */
    public boolean checkLegalMoveNotCapturingOwnPiece(Pos pos, Board board) {
        if (board.getPieceAt(pos) != null) {
            return board.getPieceAt(pos).getSide() != side;
        }
        return true;
    }

    /**
     * Checks if a move to a given position is legal based on the board state.
     * This checks if the move stays within bounds, does not capture the piece's own side, and is otherwise valid.
     * If the move is legal, it returns the target position, otherwise, it returns null.
     *
     * @param pos the position to check.
     * @param board the board on which the piece is placed.
     * @return the target position if the move is legal, or null if the move is not legal.
     */
    public Pos checkLegalMove(Pos pos, Board board) {
        boolean valid = true;
        if (pos.col() >= 0 && pos.col() <= (board.getWidth() - 1) && pos.row() >= 0 && pos.row() <= (board.getHeight() - 1)) {
            if (board.getPieceAt(pos) != null) {
                if (board.getPieceAt(pos).getSide() == side) {
                    valid = false;
                }
            }
            if (valid) {
                return pos;
            }
        }
        return null;
    }
}
