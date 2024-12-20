package model.pieces;

import model.Board;
import util.Pos;
import util.Side;

import java.util.ArrayList;

public class Bishop extends Promotable {
    private final int[][] promotedMoves = {{1,0},{-1,0},{0,1},{0,-1}};

    public Bishop(Side side) {
        super(side);
    }

//    @Override
//    protected String getImageAbbreviationLetters() {
//        return "KA";
//    }
//
//    @Override
//    protected String getPromotedImageAbbreviationLetters() {
//        return "UM";
//    }

    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int availableRow;
        int availableCol;
        int movesLength = board.getHeight();
        boolean previousPieceEnemy;

        for (int rowI = -1; rowI < 3; rowI = rowI + 2) {
            for (int colI = -1; colI < 3; colI = colI + 2) {
                previousPieceEnemy = false;
                for (int i = 1; i <= (movesLength); i ++) {
                    availableCol = pos.col() + (i * colI);
                    availableRow = pos.row() + (i * rowI);
                    if (previousPieceEnemy){
                        break;
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
        }

        if (isPromoted){
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


        for (int rowI = -1; rowI < 3; rowI = rowI + 2) {
            for (int colI = -1; colI < 3; colI = colI + 2) {
                previousPieceEnemy = false;

                ArrayList<Pos> availableMoves = new ArrayList<>();
                for (int i = 1; i <= (movesLength); i ++) {
                    availableCol = pos.col() + (i * colI);
                    availableRow = pos.row() + (i * rowI);
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
        }

        if (isPromoted){
            for (int[] promotedMove : promotedMoves) {
                availableCol = pos.col() + promotedMove[0];
                availableRow = pos.row() + promotedMove[1];
                ArrayList<Pos> availableMoves = new ArrayList<>();
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

