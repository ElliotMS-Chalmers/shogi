package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;

import java.util.ArrayList;

/**
 * Represents a King piece. The King has specific movement rules
 * and cannot be promoted. It can move one square in any direction.
 */
public class King extends Piece {

    /**
     * Movement options for the King.
     * Represented as an array of relative row and column offsets.
     */
    private final int[][] moves = {
            {-1, 1}, {0, 1}, {1, 1},
            {-1, 0},        {1, 0},
            {-1, -1}, {0, -1}, {1, -1}
    };

    /**
     * Constructs a new King with the specified side.
     *
     * @param side the side of the King (e.g., Sente or Gote).
     */
    public King(Side side) {
        super(side);
    }

    /**
     * Calculates the available moves for the King based on its current position
     * and the state of the board. This method is used specifically for backend logic
     * and includes all legal moves regardless of capturing own pieces.
     *
     * @param pos   the current position of the King.
     * @param board the current state of the board.
     * @return a list of positions representing the available backend moves for the King.
     */
    @Override
    public ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();

        // Iterate over all possible moves for the King.
        for (int[] offset : moves) {
            int availableCol = pos.col() + offset[0];
            int availableRow = pos.row() + offset[1];

            // Check if the move is within board bounds.
            if (checkLegalMoveWithinBounds(new Pos(availableRow, availableCol), board)) {
                availableMoves.add(new Pos(availableRow, availableCol));
            }
        }
        return availableMoves;
    }

    /**
     * Calculates the available moves for the King based on its current position
     * and the state of the board. This method filters out moves where the King
     * would capture a piece on its own side.
     *
     * @param pos   the current position of the King.
     * @param board the current state of the board.
     * @return a list of positions representing the available moves for the King.
     */
    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        ArrayList<Pos> availableMovesBackend = getAvailableMovesBackend(pos, board);

        // Filter out moves that would capture a piece on the same side.
        for (Pos availableMoveBackend : availableMovesBackend) {
            if (checkLegalMoveNotCapturingOwnPiece(
                    new Pos(availableMoveBackend.row(), availableMoveBackend.col()), board)) {
                availableMoves.add(new Pos(availableMoveBackend.row(), availableMoveBackend.col()));
            }
        }
        return availableMoves;
    }
}
