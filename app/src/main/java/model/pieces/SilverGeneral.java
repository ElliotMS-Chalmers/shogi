package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;

import java.util.ArrayList;

/**
 * Class representing a Silver General piece. It moves one square diagonally, or one square forward or backward.
 * When promoted, it gains the ability to move like a Gold General.
 */
public class SilverGeneral extends Promotable {
    // Array of basic moves for the Silver General
    private final int[][] moves = {{-1, 1}, {-1, -1}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    // Array of promoted moves for the Silver General (Gold General-like movement)
    private final int[][] promotedMoves = {{-1, 1}, {-1, -1}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 0}, {-1, 0}, {1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Constructs a new Silver General piece with the specified side.
     *
     * @param side the side of the Silver General piece (SENTE or GOTE).
     */
    public SilverGeneral(Side side) {
        super(side);
    }

    /**
     * Returns the available moves for the Silver General piece in the backend.
     *
     * The Silver General moves one square diagonally or one square forward or backward. Upon promotion,
     * it gains the ability to move like a Gold General. The method checks for legal moves by ensuring that
     * the destination square is within bounds and not occupied by a piece of the same side.
     *
     * @param pos the current position of the Silver General.
     * @param board the board on which the Silver General is placed.
     * @return a list of available positions the Silver General can move to.
     */
    @Override
    public ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int team = 0;
        int availableCol;
        int availableRow;
        int movesLength;

        // Determine the team's side (SENTE or GOTE)
        if (side == Side.SENTE) {
            team = 1;
        }

        // Select the appropriate moves based on whether the Silver General is promoted
        if (isPromoted) {
            movesLength = promotedMoves.length;
        } else {
            movesLength = moves.length;
        }

        // Iterate through all possible moves (half of the array for each direction)
        for (int i = 0; i < (movesLength / 2); i++) {
            if (isPromoted) {
                availableCol = pos.col() + promotedMoves[i * 2 + team][0];
                availableRow = pos.row() + promotedMoves[i * 2 + team][1];
            } else {
                availableCol = pos.col() + moves[i * 2 + team][0];
                availableRow = pos.row() + moves[i * 2 + team][1];
            }

            // Check if the move is legal and within bounds
            if (checkLegalMoveWithinBounds(new Pos(availableRow, availableCol), board)) {
                availableMoves.add(new Pos(availableRow, availableCol));
            }
        }

        return availableMoves;
    }

    /**
     * Returns the available moves for the Silver General piece, excluding moves that capture friendly pieces.
     *
     * This method filters out moves that would result in capturing a piece of the same side. It calls the
     * `getAvailableMovesBackend` method to get all available moves, then checks whether the move would capture
     * a friendly piece. Only valid moves are added to the list.
     *
     * @param pos the current position of the Silver General.
     * @param board the board on which the Silver General is placed.
     * @return a list of available positions the Silver General can move to.
     */
    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        ArrayList<Pos> availableMovesBackend = getAvailableMovesBackend(pos, board);

        // Filter out moves that would capture a friendly piece
        for (Pos availableMoveBackend : availableMovesBackend) {
            if (checkLegalMoveNotCapturingOwnPiece(new Pos(availableMoveBackend.row(), availableMoveBackend.col()), board)) {
                availableMoves.add(new Pos(availableMoveBackend.row(), availableMoveBackend.col()));
            }
        }

        return availableMoves;
    }
}
