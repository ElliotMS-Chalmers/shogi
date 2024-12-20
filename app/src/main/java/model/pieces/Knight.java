package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;

import java.util.ArrayList;

/**
 * Represents a Knight piece. The Knight has unique movement
 * rules and can also be promoted to gain additional moves.
 */
public class Knight extends Promotable {

    /**
     * Movement options for the Knight in its unpromoted state.
     * Represented as an array of relative row and column offsets.
     */
    private final int[][] moves = {
            {-1, 2}, {-1, -2},
            {1, 2}, {1, -2}
    };

    /**
     * Movement options for the Knight in its promoted state.
     * Represented as an array of relative row and column offsets.
     */
    private final int[][] promotedMoves = {
            {-1, 1}, {-1, -1}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 0}, {-1, 0},
            {1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    /**
     * Constructs a new Knight with the specified side.
     *
     * @param side the side of the Knight (e.g., Sente or Gote).
     */
    public Knight(Side side) {
        super(side);
    }

    /**
     * Returns the SFEN (Shogi Forsyth–Edwards Notation) abbreviation for the Knight.
     * Unpromoted Knights are represented by 'N' for Sente and 'n' for Gote.
     *
     * @return the SFEN abbreviation for the Knight.
     */
    @Override
    public String getSfenAbbreviation() {
        char letter = 'N';
        String abbr = switch (side) {
            case SENTE -> String.valueOf(letter);
            case GOTE -> String.valueOf(letter).toLowerCase();
        };
        return isPromoted ? '+' + abbr : abbr;
    }

    /**
     * Calculates the available moves for the Knight based on its current position
     * and the state of the board. This method is used specifically for backend logic
     * and includes all legal moves regardless of capturing own pieces.
     *
     * @param pos   the current position of the Knight.
     * @param board the current state of the board.
     * @return a list of positions representing the available backend moves for the Knight.
     */
    @Override
    public ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int team = (side == Side.SENTE) ? 1 : 0;
        int movesLength = isPromoted ? promotedMoves.length : moves.length;

        // Iterate over possible moves based on promotion state.
        for (int i = 0; i < (movesLength / 2); i++) {
            int availableCol;
            int availableRow;

            if (isPromoted) {
                availableCol = pos.col() + promotedMoves[i * 2 + team][0];
                availableRow = pos.row() + promotedMoves[i * 2 + team][1];
            } else {
                availableCol = pos.col() + moves[i * 2 + team][0];
                availableRow = pos.row() + moves[i * 2 + team][1];
            }

            // Check if the move is within board bounds.
            if (checkLegalMoveWithinBounds(new Pos(availableRow, availableCol), board)) {
                availableMoves.add(new Pos(availableRow, availableCol));
            }
        }
        return availableMoves;
    }

    /**
     * Calculates the available moves for the Knight based on its current position
     * and the state of the board. This method filters out moves where the Knight
     * would capture a piece on its own side.
     *
     * @param pos   the current position of the Knight.
     * @param board the current state of the board.
     * @return a list of positions representing the available moves for the Knight.
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
