package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;

import java.util.ArrayList;

/**
 * Class representing a Rook piece in Shogi. It moves any number of squares horizontally or vertically.
 * When promoted, the Rook gains the ability to move diagonally as well.
 */
public class Rook extends Promotable {
    private final int[][] promotedMoves = {{1,1},{-1,1},{1,-1},{-1,-1}};

    /**
     * Constructs a new Rook piece with the specified side.
     *
     * @param side the side of the Rook piece (SENTE or GOTE).
     */
    public Rook(Side side) {
        super(side);
    }

    /**
     * Returns the available moves for the Rook piece on the board.
     *
     * The Rook moves any number of squares vertically or horizontally. If promoted, it gains the ability
     * to move diagonally. The method checks for legal moves by verifying if the destination square is
     * within bounds and not occupied by a piece of the same side.
     *
     * @param pos the current position of the Rook.
     * @param board the board on which the Rook is placed.
     * @return a list of available positions the Rook can move to.
     */
    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int availableRow;
        int availableCol;
        int movesLength = board.getHeight();
        boolean previousPieceEnemy;
        int[] test = {-1, 0, 0, 1};

        // Check vertical and horizontal moves
        for (int rowI = 0; rowI < 4; rowI++) {
            previousPieceEnemy = false;
            for (int i = 1; i <= movesLength; i++) {
                availableCol = pos.col() + (i * test[(rowI + 2) % 4]);
                availableRow = pos.row() + (i * test[rowI % 4]);
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
                if (checkLegalMove(new Pos(availableRow, availableCol), board) == null) {
                    break;
                } else {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }
            }
        }

        // Check promoted moves (diagonal)
        if (isPromoted) {
            for (int[] promotedMove : promotedMoves) {
                availableCol = pos.col() + promotedMove[0];
                availableRow = pos.row() + promotedMove[1];
                if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }
            }
        }

        return availableMoves;
    }

    /**
     * Returns the available moves for the Rook piece, including special conditions for backend logic.
     *
     * This method is similar to `getAvailableMoves()`, but it also considers specific backend conditions
     * for legal moves. It checks whether the Rook can move to a square, considering additional game logic such as
     * the presence of the enemy's King.
     *
     * @param pos the current position of the Rook.
     * @param board the board on which the Rook is placed.
     * @return a list of available positions the Rook can move to.
     */
    @Override
    public ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int availableRow;
        int availableCol;
        int movesLength = board.getHeight();
        boolean previousPieceEnemy;
        boolean previousPieceEnemyking;
        boolean previousPieceFriend;
        int[] directions = {-1,0,0,1};

        for (int rowI = 0; rowI < 4; rowI ++) {
            previousPieceEnemy = false;
            previousPieceEnemyking = false;
            previousPieceFriend = false;
            for (int i = 1; i <= (movesLength); i ++) {
                availableCol = (pos.col() + (i * directions[(rowI+2)%4]));
                availableRow = (pos.row() + (i * directions[rowI%4]));
                if (previousPieceEnemy){
                    if (checkLegalMoveWithinBounds(new Pos(availableRow, availableCol), board) && previousPieceEnemyking) {
                        availableMoves.add(new Pos(availableRow, availableCol));
                    }
                    break;
                } else if (previousPieceFriend) {
                    break;
                }
                if (checkLegalMoveWithinBounds(new Pos(availableRow, availableCol), board)) {
                    if (board.getPieceAt(new Pos(availableRow, availableCol)) != null) {
                        if (board.getPieceAt(new Pos(availableRow, availableCol)).getSide() != side) {
                            previousPieceEnemy = true;
                            if (board.getPieceAt(new Pos(availableRow, availableCol)).getClass() == King.class){
                                previousPieceEnemyking = true;
                            }
                        } else if (board.getPieceAt(new Pos(availableRow, availableCol)).getSide() == side) {
                            previousPieceFriend = true;
                        }
                    }
                }
                if (!checkLegalMoveWithinBounds(new Pos(availableRow, availableCol), board)){
                    break;
                } else {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }

            }
        }


        if (isPromoted){
            for (int[] promotedMove : promotedMoves) {
                availableCol = pos.col() + promotedMove[0];
                availableRow = pos.row() + promotedMove[1];
                if (checkLegalMoveWithinBounds(new Pos(availableRow, availableCol), board)) {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }
            }
        }
        return availableMoves;
    }

    @Override
    public ArrayList<Pos> getForcingCheckMoves(Pos pos, Pos kingPos, Board board){
        int availableRow;
        int availableCol;
        int movesLength = board.getHeight();
        boolean previousPieceEnemy;
        int[] directions = {-1,0,0,1};

        for (int rowI = 0; rowI < 4; rowI ++) {
            ArrayList<Pos> availableMoves = new ArrayList<>();
            previousPieceEnemy = false;
            for (int i = 1; i <= (movesLength); i ++) {
                availableCol = (pos.col() + (i * directions[(rowI+2)%4]));
                availableRow = (pos.row() + (i * directions[rowI%4]));
                if (previousPieceEnemy){
                    return availableMoves;
                }
                if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                    if (board.getPieceAt(new Pos(availableRow, availableCol)) != null) {
                        if (board.getPieceAt(new Pos(availableRow, availableCol)).getSide() != side) {
                            previousPieceEnemy = true;
                        }
                    }
                }
                if (checkLegalMove(new Pos(availableRow, availableCol), board) == null){
                    break;
                } else {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }

            }
        }


        if (isPromoted){
            for (int[] promotedMove : promotedMoves) {
                ArrayList<Pos> availableMoves = new ArrayList<>();
                availableCol = pos.col() + promotedMove[0];
                availableRow = pos.row() + promotedMove[1];
                if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                    if (board.getPieceAt(new Pos(availableRow, availableCol)).getClass() == King.class) {
                        availableMoves.add(new Pos(availableRow, availableCol));
                        return availableMoves;
                    }
                }
            }
        }
        return null;
    }
}
