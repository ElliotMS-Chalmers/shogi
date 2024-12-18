package model.pieces;

import model.Board;
import util.Pos;
import util.Side;

import java.util.ArrayList;

public class Rook extends Promotable {
    private final int[][] promotedMoves = {{1,1},{-1,1},{1,-1},{-1,-1}};

    public Rook(Side side) {
        super(side);
    }

//    @Override
//    protected String getImageAbbreviationLetters() {
//        return "HI";
//    }
//
//    @Override
//    protected String getPromotedImageAbbreviationLetters() {
//        return "RY";
//    }

    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int availableRow;
        int availableCol;
        int movesLength = board.getHeight();
        boolean previousPieceEnemy;
        int[] test = {-1,0,0,1};

        for (int rowI = 0; rowI < 4; rowI ++) {
                previousPieceEnemy = false;
                for (int i = 1; i <= (movesLength); i ++) {
                    availableCol = (pos.col() + (i * test[(rowI+2)%4]));
                    availableRow = (pos.row() + (i * test[rowI%4]));
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
        int[] directions = {-1,0,0,1};

        for (int rowI = 0; rowI < 4; rowI ++) {
            previousPieceEnemy = false;
            previousPieceEnemyking = false;
            for (int i = 1; i <= (movesLength); i ++) {
                availableCol = (pos.col() + (i * directions[(rowI+2)%4]));
                availableRow = (pos.row() + (i * directions[rowI%4]));
                if (previousPieceEnemy){
                    if (checkLegalMove(new Pos(availableRow, availableCol), board) != null && previousPieceEnemyking) {
                        availableMoves.add(new Pos(availableRow, availableCol));
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
                if (checkLegalMove(new Pos(availableRow, availableCol), board) == null){
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
                if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }
            }
        }
        return availableMoves;
    }
}
