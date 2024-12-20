package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;

import java.util.ArrayList;

/**
 * Represents a Copper General piece.
 */
public class CopperGeneral extends Promotable {

    /**
     * Standard movement options for the Copper General.
     * Represented as an array of relative row and column offsets.
     */
    private final int[][] moves = {{-1, 1}, {-1, -1}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {0, -1}, {0, 1}};

    /**
     * Additional movement options for the Copper General when promoted.
     * Currently, no additional moves are defined.
     */
    private final int[][] promotedMoves = {};

    /**
     * Constructs a new Copper General with the specified side.
     *
     * @param side the side of the Copper General (e.g., sente or gote).
     */
    public CopperGeneral(Side side) {
        super(side);
    }

    /**
     * Calculates the available moves for the Copper General based on its current position
     * and the state of the board.
     *
     * @param pos   the current position of the Copper General.
     * @param board the current state of the board.
     * @return a list of positions representing the available moves for the Copper General.
     */
    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int team = 0; // Used to determine the direction of movement based on the side.
        int availableCol;
        int availableRow;
        int movesLength;

        // Set the direction of movement for Sente (default is Gote).
        if (side == Side.SENTE) {
            team = 1;
        }

        // Determine which set of moves to use based on promotion status.
        if (isPromoted) {
            movesLength = promotedMoves.length;
        } else {
            movesLength = moves.length;
        }

        // Calculate all valid moves.
        for (int i = 0; i < (movesLength / 2); i++) {
            if (isPromoted) {
                availableCol = pos.col() + promotedMoves[i * 2 + team][0];
                availableRow = pos.row() + promotedMoves[i * 2 + team][1];
            } else {
                availableCol = pos.col() + moves[i * 2 + team][0];
                availableRow = pos.row() + moves[i * 2 + team][1];
            }

            // Check if the move is legal and add it to the list of available moves.
            if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                availableMoves.add(new Pos(availableRow, availableCol));
            }
        }

        return availableMoves;
    }

    /**
     * Calculates the available moves for the Copper General with additional backend-specific
     * considerations. This method is currently identical to {@link #getAvailableMoves(Pos, Board)}.
     *
     * @param pos   the current position of the Copper General.
     * @param board the current state of the board.
     * @return a list of positions representing the available moves for the Copper General,
     *         including backend-specific conditions.
     */
    @Override
    public ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board) {
        return getAvailableMoves(pos, board);
    }
}
