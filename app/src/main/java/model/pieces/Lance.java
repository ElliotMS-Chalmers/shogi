package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;

import java.util.ArrayList;

/**
 * The Lance class represents the Lance piece. It can move vertically along a file but only in one direction (towards the opponent's side),
 * and its promoted form gains additional movement capabilities similar to other general pieces.
 */
public class Lance extends Promotable {

    // The movement options for the Lance's promoted form
    private final int[][] promotedMoves = {{-1,1},{-1,-1}, {0,1},{0,-1}, {1,1},{1,-1}, {-1,0},{-1,0}, {1,0},{1,0}, {0,-1},{0,1}};

    /**
     * Constructs a Lance piece for a specific side (SENTE or GOTE).
     *
     * @param side the side of the player (SENTE or GOTE) controlling the Lance piece.
     */
    public Lance(Side side) {
        super(side);
    }

    /**
     * Returns the list of available moves for the Lance piece, depending on whether it is promoted.
     *
     * If the Lance is promoted, it moves similarly to a Gold General, with additional movement options.
     * Otherwise, the Lance moves vertically in one direction, capturing enemy pieces along the way.
     *
     * @param pos the current position of the Lance piece on the board.
     * @param board the board on which the piece is placed, used for checking legality of moves.
     * @return an ArrayList of valid positions the Lance can move to.
     */
    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int team = 0;
        int availableCol;
        int availableRow;
        int movesLength = board.getWidth();
        boolean previousPieceEnemy;

        // Set the team direction based on the piece's side (SENTE or GOTE).
        if (side == Side.SENTE) {
            team = 1;
        }

        // Check for promoted Lance moves
        if (isPromoted) {
            for (int i = 0; i < (promotedMoves.length / 2); i++) {
                availableCol = pos.col() + promotedMoves[i * 2 + team][0];
                availableRow = pos.row() + promotedMoves[i * 2 + team][1];
                if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }
            }
        } else {
            previousPieceEnemy = false;
            for (int i = 1; i <= movesLength; i++) {
                // The Lance moves vertically along a column in one direction.
                availableCol = (pos.col());
                availableRow = (pos.row() + (i * -1 * (-1 + team * 2)));

                // Stop if the piece encounters an enemy piece.
                if (previousPieceEnemy) {
                    break;
                }

                if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                    if (board.getPieceAt(new Pos(availableRow, availableCol)) != null) {
                        if (board.getPieceAt(new Pos(availableRow, availableCol)).getSide() != side) {
                            previousPieceEnemy = true;
                        }
                    }
                }

                // Stop if the move goes outside the board or encounters an obstacle.
                if (checkLegalMove(new Pos(availableRow, availableCol), board) == null) {
                    break;
                } else {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }
            }
        }

        return availableMoves;
    }

    /**
     * Returns the list of available moves for the Lance piece, considering promotion.
     *
     * This method is used for internal logic, such as calculating legal moves for the Lance,
     * and includes handling the Lance's promoted movement capabilities.
     *
     * @param pos the current position of the Lance piece on the board.
     * @param board the board on which the piece is placed, used for checking legality of moves.
     * @return an ArrayList of valid positions the Lance can move to, considering promotion.
     */
    @Override
    public ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int team = 0;
        int availableCol;
        int availableRow;
        int movesLength = board.getWidth();
        boolean previousPieceEnemy;
        boolean previousPieceEnemyKing;

        if (side == Side.SENTE) {
            team = 1;
        }

        // Check for promoted Lance moves
        if (isPromoted) {
            for (int i = 0; i < (promotedMoves.length / 2); i++) {
                availableCol = pos.col() + promotedMoves[i * 2 + team][0];
                availableRow = pos.row() + promotedMoves[i * 2 + team][1];
                if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }
            }
        } else {
            previousPieceEnemy = false;
            previousPieceEnemyKing = false;

            for (int i = 1; i <= movesLength; i++) {
                availableCol = (pos.col());
                availableRow = (pos.row() + (i * -1 * (-1 + team * 2)));

                if (previousPieceEnemy) {
                    if (checkLegalMove(new Pos(availableRow, availableCol), board) != null && previousPieceEnemyKing) {
                        availableMoves.add(new Pos(availableRow, availableCol));
                    }
                    break;
                }

                if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                    if (board.getPieceAt(new Pos(availableRow, availableCol)) != null) {
                        if (board.getPieceAt(new Pos(availableRow, availableCol)).getSide() != side) {
                            previousPieceEnemy = true;
                            if (board.getPieceAt(new Pos(availableRow, availableCol)).getClass() == King.class) {
                                previousPieceEnemyKing = true;
                            }
                        }
                    }
                }

                if (checkLegalMove(new Pos(availableRow, availableCol), board) == null) {
                    break;
                } else {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }
            }
        }

        return availableMoves;
    }
}
