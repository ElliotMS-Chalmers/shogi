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
}
