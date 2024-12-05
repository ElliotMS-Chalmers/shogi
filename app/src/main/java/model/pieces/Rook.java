package model.pieces;

import util.Pos;
import util.Side;

import java.util.ArrayList;

public class Rook extends Promotable {
    private final int[][] moves = {{0,1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},{0,8},{0,-1},{0,-2},{0,-3},{0,-4},{0,-5},{0,-6},{0,-7},{0,-8},{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},{8,0},{-1,0},{-2,0},{-3,0},{-4,0},{-5,0},{-6,0},{-7,0},{-8,0}};

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
