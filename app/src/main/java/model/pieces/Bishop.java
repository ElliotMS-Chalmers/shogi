package model.pieces;

import util.Pos;
import util.Side;

import java.util.ArrayList;

public class Bishop extends Promotable {
    private final int[][] moves = {{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7},{8,8},{-1,1},{-2,2},{-3,3},{-4,4},{-5,5},{-6,6},{-7,7},{-8,8},{-1,-1},{-2,-2},{-3,-3},{-4,-4},{-5,-5},{-6,-6},{-7,-7},{-8,-8},{1,-1},{2,-2},{3,-3},{4,-4},{5,-5},{6,-6},{7,-7},{8,-8}};

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
    public ArrayList<ArrayList<Integer>> getAvailableMoves(Pos pos, Side side) {
        ArrayList<ArrayList<Integer>> availableMoves = new ArrayList<>();
        for (int[] ints : moves) {
            int availableCol = pos.col() + ints[0];
            int availableRow = pos.row() + ints[1];
            if (availableCol >= 0 && availableCol <= 8 && availableRow >= 0 && availableRow <= 8) {
                ArrayList<Integer> move = new ArrayList<>();
                move.add(availableCol);
                move.add(availableRow);
                availableMoves.add(move);
            }
        }
        return availableMoves;
    }
}
