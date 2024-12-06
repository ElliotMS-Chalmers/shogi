package model.pieces;

import util.Pos;
import util.Side;

import java.util.ArrayList;

public class Lance extends Promotable {
    private final int[][] moves = {{0,1},{0,-1}, {0,2},{0,-2}, {0,3},{0,-3}, {0,4},{0,-4}, {0,5},{0,-5}, {0,6},{0,-6}, {0,7},{0,-7}, {0,8},{0,-8}};
    private final int[][] promotedMoves = {{-1,1},{-1,-1}, {0,1},{0,-1}, {1,1},{1,-1}, {-1,0},{-1,0} ,{1,0},{1,0}, {0,-1},{0,1}};
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
        int team = 0;
        int availableCol;
        int availableRow;
        if (side == Side.SENTE){
            team = 1;
        }
        for (int i = 0; i < (moves.length/2); i ++) {
            if (this.getIsPromoted()){
                availableCol = pos.col() + promotedMoves[i*2 + team][0];
                availableRow = pos.row() + promotedMoves[i*2 + team][1];
            } else {
                availableCol = pos.col() + moves[i * 2 + team][0];
                availableRow = pos.row() + moves[i * 2 + team][1];
            }
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
