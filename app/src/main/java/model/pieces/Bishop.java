package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;

import java.util.ArrayList;

/**
 * Represents a Bishop piece The Bishop can move diagonally
 * across the board and gains additional movement options when promoted.
 */
public class Bishop extends Promotable {

    /**
     * Additional movement options when the Bishop is promoted.
     * Represented as an array of relative row and column offsets.
     */
    private final int[][] promotedMoves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    /**
     * Constructs a new Bishop with the specified side.
     *
     * @param side the side of the Bishop (e.g., white or black).
     */
    public Bishop(Side side) {
        super(side);
    }

    /**
     * Calculates the available moves for the Bishop based on its current position
     * and the state of the board.
     *
     * @param pos   the current position of the Bishop.
     * @param board the current state of the board.
     * @return a list of positions representing the available moves for the Bishop.
     */
    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int availableRow;
        int availableCol;
        int movesLength = board.getHeight();
        boolean previousPieceEnemy;

        // Check diagonal moves
        for (int rowI = -1; rowI < 3; rowI += 2) {
            for (int colI = -1; colI < 3; colI += 2) {
                previousPieceEnemy = false;
                for (int i = 1; i <= movesLength; i++) {
                    availableCol = pos.col() + (i * colI);
                    availableRow = pos.row() + (i * rowI);
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
        }

        // Add additional moves if promoted
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
     * Calculates the available moves for the Bishop with additional backend-specific
     * considerations (e.g., King interactions).
     *
     * @param pos   the current position of the Bishop.
     * @param board the current state of the board.
     * @return a list of positions representing the available moves for the Bishop,
     *         including backend-specific conditions.
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

        for (int rowI = -1; rowI < 3; rowI = rowI + 2) {
            for (int colI = -1; colI < 3; colI = colI + 2) {
                previousPieceEnemy = false;
                previousPieceEnemyking = false;
                previousPieceFriend = false;
                for (int i = 1; i <= (movesLength); i ++) {
                    availableCol = pos.col() + (i * colI);
                    availableRow = pos.row() + (i * rowI);
                    if (previousPieceEnemy){
                        if (checkLegalMoveWithinBounds(new Pos(availableRow, availableCol), board) && previousPieceEnemyking){
                            availableMoves.add(new Pos(availableRow, availableCol));
                        }
                        break;
                    } else if (previousPieceFriend){
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
        boolean previousPieceEnemyking;

        for (int rowI = -1; rowI < 3; rowI = rowI + 2) {
            for (int colI = -1; colI < 3; colI = colI + 2) {
                previousPieceEnemy = false;
                previousPieceEnemyking = false;
                ArrayList<Pos> availableMoves = new ArrayList<>();
                for (int i = 1; i <= (movesLength); i ++) {
                    availableCol = pos.col() + (i * colI);
                    availableRow = pos.row() + (i * rowI);
                    if (previousPieceEnemy){
                        if (previousPieceEnemyking){
                            return availableMoves;
                        }
                        break;
                    }
                    if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                        if (board.getPieceAt(new Pos(availableRow, availableCol)) != null) {
                            if (board.getPieceAt(new Pos(availableRow, availableCol)).getSide() != side) {
                                previousPieceEnemy = true;
                                if (board.getPieceAt(new Pos(availableRow, availableCol)).getClass() == King.class){
                                    previousPieceEnemyking = true;
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
        }

        // Add additional moves if promoted
        if (isPromoted) {
            for (int[] promotedMove : promotedMoves) {
                availableCol = pos.col() + promotedMove[0];
                availableRow = pos.row() + promotedMove[1];
                ArrayList<Pos> availableMoves = new ArrayList<>();
                if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                    if (board.getPieceAt(new Pos(availableRow, availableCol)) != null) {
                        if (board.getPieceAt(new Pos(availableRow, availableCol)).getClass() == King.class) {
                            availableMoves.add(new Pos(availableRow, availableCol));
                            return availableMoves;
                        }
                    }
                }
            }
        }
        return null;
    }
}

