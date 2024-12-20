package model.pieces;

import model.game.Board;
import util.Side;
import util.Pos;

import java.util.ArrayList;

/**
 * The Pawn class represents the Pawn piece. It moves one square forward, but only in a straight line.
 * A promoted Pawn behaves like a Gold General, gaining more movement options.
 */
public class Pawn extends Promotable {

    // The movement options for the Pawn's unpromoted form (moves forward one step)
    private final int[][] moves = {{0,1}, {0,-1}};

    // The movement options for the Pawn's promoted form (moves like a Gold General)
    private final int[][] promotedMoves = {{-1,1},{-1,-1}, {0,1},{0,-1}, {1,1},{1,-1}, {-1,0},{-1,0}, {1,0},{1,0}, {0,-1},{0,1}};

    /**
     * Constructs a Pawn piece for a specific side (SENTE or GOTE).
     *
     * @param side the side of the player (SENTE or GOTE) controlling the Pawn piece.
     */
    public Pawn(Side side) {
        super(side);
    }

    /**
     * Returns the list of available moves for the Pawn piece, considering its promotion status.
     *
     * If the Pawn is promoted, it gains additional movement options, including diagonal moves
     * and the ability to move forward one square in any direction. Otherwise, it only moves forward
     * one square in the direction of the opponent.
     *
     * @param pos the current position of the Pawn piece on the board.
     * @param board the board on which the piece is placed, used for checking legality of moves.
     * @return an ArrayList of valid positions the Pawn can move to, considering promotion.
     */
    @Override
    public ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int team = 0;
        int availableCol;
        int availableRow;
        int movesLength;

        // Set the team direction based on the piece's side (SENTE or GOTE).
        if (side == Side.SENTE) {
            team = 1;
        }

        // Determine the number of possible moves based on promotion status
        if (isPromoted) {
            movesLength = promotedMoves.length;
        } else {
            movesLength = moves.length;
        }

        // Loop through possible moves and add legal ones to availableMoves
        for (int i = 0; i < (movesLength / 2); i++) {
            if (isPromoted) {
                availableCol = pos.col() + promotedMoves[i * 2 + team][0];
                availableRow = pos.row() + promotedMoves[i * 2 + team][1];
            } else {
                availableCol = pos.col() + moves[i * 2 + team][0];
                availableRow = pos.row() + moves[i * 2 + team][1];
            }
            // Check if the move is within bounds and legal
            if (checkLegalMoveWithinBounds(new Pos(availableRow, availableCol), board)) {
                availableMoves.add(new Pos(availableRow, availableCol));
            }
        }

        return availableMoves;
    }

    /**
     * Returns the list of available moves for the Pawn piece, excluding moves that would capture its own piece.
     *
     * This method is a wrapper around `getAvailableMovesBackend` and ensures that moves which result
     * in capturing the Pawn's own pieces are excluded from the final list of available moves.
     *
     * @param pos the current position of the Pawn piece on the board.
     * @param board the board on which the piece is placed, used for checking legality of moves.
     * @return an ArrayList of valid positions the Pawn can move to, excluding self-captures.
     */
    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        ArrayList<Pos> availableMovesBackend = getAvailableMovesBackend(pos, board);

        // Loop through backend moves and filter out any that result in capturing the Pawn's own piece
        for (Pos availableMoveBackend : availableMovesBackend) {
            if (checkLegalMoveNotCapturingOwnPiece(new Pos(availableMoveBackend.row(), availableMoveBackend.col()), board)) {
                availableMoves.add(new Pos(availableMoveBackend.row(), availableMoveBackend.col()));
            }
        }

        return availableMoves;
    }
}
