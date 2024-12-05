package model.pieces;

import util.Pos;
import util.Side;

import java.util.ArrayList;

public class Lance extends Promotable {
    private final int[][] moves = {{0,1},{0,-1}, {0,2},{0,-2}, {0,3},{0,-3}, {0,4},{0,-4}, {0,5},{0,-5}, {0,6},{0,-6}, {0,7},{0,-7}, {0,8},{0,-8}};
    public Lance(Side side) {
        super(side);
    }

    @Override
    protected String getImageAbbreviationLetters() {
        return "KY";
    }

    @Override
    protected String getPromotedImageAbbreviationLetters() {
        return "NY";
    }

    @Override
    public ArrayList<ArrayList<Integer>> getAvailableMoves(Pos pos, Side side){
        ArrayList<ArrayList<Integer>> availableMoves = new ArrayList<>();
        int team = 1;
        if (side == Side.SENTE){
            team = 0;
        }
        for (int i = 0; i < (moves.length/2); i ++) {
            int availableCol = pos.col() + moves[i*2 + team][0];
            int availableRow = pos.row() + moves[i*2 + team][1];
            if (availableCol >= 0 && availableCol <= 8 && availableRow >= 0 && availableRow <= 8){
                ArrayList<Integer> move = new ArrayList<>();
                move.add(availableCol);
                move.add(availableRow);
                availableMoves.add(move);
            }
        }
        return availableMoves;
    }
}
