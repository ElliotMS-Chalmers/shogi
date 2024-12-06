package model.pieces;

import util.Pos;
import util.Side;

import java.util.ArrayList;

public class Rook extends Promotable {
    private final int[][] moves = {{0,1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},{0,8},{0,-1},{0,-2},{0,-3},{0,-4},{0,-5},{0,-6},{0,-7},{0,-8},{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},{8,0},{-1,0},{-2,0},{-3,0},{-4,0},{-5,0},{-6,0},{-7,0},{-8,0}};
    private final int[][] promotedMoves = {{0,1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},{0,8},{0,-1},{0,-2},{0,-3},{0,-4},{0,-5},{0,-6},{0,-7},{0,-8},{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},{8,0},{-1,0},{-2,0},{-3,0},{-4,0},{-5,0},{-6,0},{-7,0},{-8,0},{1,1},{-1,1},{1,-1},{-1,-1}};

    public Rook(Side side) {
        super(side);
    }

    @Override
    protected String getImageAbbreviationLetters() {
        return "HI";
    }

    @Override
    protected String getPromotedImageAbbreviationLetters() {
        return "RY";
    }

    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int availableCol;
        int availableRow;
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
