package model.pieces;

import util.Pos;
import util.Side;

import java.util.ArrayList;

public class Bishop extends Promotable {
    private final int[][] moves = {{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7},{8,8},{-1,1},{-2,2},{-3,3},{-4,4},{-5,5},{-6,6},{-7,7},{-8,8},{-1,-1},{-2,-2},{-3,-3},{-4,-4},{-5,-5},{-6,-6},{-7,-7},{-8,-8},{1,-1},{2,-2},{3,-3},{4,-4},{5,-5},{6,-6},{7,-7},{8,-8}};
    private final int[][] promotedMoves = {{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7},{8,8},{-1,1},{-2,2},{-3,3},{-4,4},{-5,5},{-6,6},{-7,7},{-8,8},{-1,-1},{-2,-2},{-3,-3},{-4,-4},{-5,-5},{-6,-6},{-7,-7},{-8,-8},{1,-1},{2,-2},{3,-3},{4,-4},{5,-5},{6,-6},{7,-7},{8,-8},{1,0},{-1,0},{0,1},{0,-1}};

    public Bishop(Side side) {
        super(side);
    }

    @Override
    protected String getImageAbbreviationLetters() {
        return "KA";
    }

    @Override
    protected String getPromotedImageAbbreviationLetters() {
        return "UM";
    }

    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int availableRow;
        int availableCol;
        int movesLength;
        if (isPromoted){
            movesLength = promotedMoves.length;
        } else {
            movesLength = moves.length;
        }

        for (int i = 0; i < (movesLength); i ++) {
            if (isPromoted){
                availableCol = pos.col() + promotedMoves[i][0];
                availableRow = pos.row() + promotedMoves[i][1];
            } else {
                availableCol = pos.col() + moves[i][0];
                availableRow = pos.row() + moves[i][1];
            }
            if (availableCol >= 0 && availableCol <= 8 && availableRow >= 0 && availableRow <= 8) {
                availableMoves.add(new Pos(availableRow,availableCol));
            }
        }
        return availableMoves;
    }
}
