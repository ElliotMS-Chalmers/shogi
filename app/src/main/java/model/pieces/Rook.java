package model.pieces;

import model.Board;
import model.variants.Variant;
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
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board, Variant variant) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int availableRow;
        int availableCol;
        int movesLength = variant.getHeight();
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
                    if (checkLegalMove(new Pos(availableRow, availableCol), board, variant) != null) {
                        if (board.getPieceAt(new Pos(availableRow, availableCol)) != null) {
                            if (board.getPieceAt(new Pos(availableRow, availableCol)).getSide() != side) {
                                previousPieceEnemy = true;
                            }
                        }
                    }
                    if (checkLegalMove(new Pos(availableRow, availableCol), board, variant) == null){
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
                if (checkLegalMove(new Pos(availableRow, availableCol), board, variant) != null) {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }
            }
        }
        return availableMoves;
    }
}
