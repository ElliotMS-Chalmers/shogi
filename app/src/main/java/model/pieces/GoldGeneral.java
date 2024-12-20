package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;

import java.util.ArrayList;

/**
 * Represents a Gold General piece. The Gold General has
 * specific movement rules and cannot be promoted.
 */
public class GoldGeneral extends Piece {

    /**
     * Movement options for the Gold General.
     * Represented as an array of relative row and column offsets.
     * The options differ slightly based on the side (Sente or Gote).
     */
    private final int[][] moves = {
            {-1, 1}, {-1, -1}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 0}, {-1, 0},
            {1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    /**
     * Constructs a new Gold General with the specified side.
     *
     * @param side the side of the Gold General (e.g., Sente or Gote).
     */
    public GoldGeneral(Side side) {
        super(side);
    }

    /**
     * Calculates the available moves for the Gold General based on its current position
     * and the state of the board. This method is used specifically for backend logic
     * and includes all legal moves regardless of capturing own pieces.
     *
     * @param pos   the current position of the Gold General.
     * @param board the current state of the board.
     * @return a list of positions representing the available backend moves for the Gold General.
     */
    @Override
    public ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int team = 0; // Default direction for Gote.
        if (side == Side.SENTE) {
            team = 1; // Adjust movement for Sente.
        }

        // Calculate all legal moves based on the predefined movement set.
        for (int i = 0; i < (moves.length / 2); i++) {
            int availableCol = pos.col() + moves[i * 2 + team][0];
            int availableRow = pos.row() + moves[i * 2 + team][1];
            if (checkLegalMoveWithinBounds(new Pos(availableRow, availableCol), board)) {
                availableMoves.add(new Pos(availableRow, availableCol));
            }
        }
        return availableMoves;
    }

    /**
     * Calculates the available moves for the Gold General based on its current position
     * and the state of the board. This method filters out moves where the Gold General
     * would capture a piece on its own side.
     *
     * @param pos   the current position of the Gold General.
     * @param board the current state of the board.
     * @return a list of positions representing the available moves for the Gold General.
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
