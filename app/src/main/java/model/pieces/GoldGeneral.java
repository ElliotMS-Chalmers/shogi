package model.pieces;

import util.Pos;
import util.Side;

import java.util.ArrayList;

public class GoldGeneral extends Piece {
    private final int[][] moves = {{-1,1},{-1,-1}, {0,1},{0,-1}, {1,1},{1,-1}, {-1,0},{-1,0} ,{1,0},{1,0}, {0,-1},{0,1}};

    public GoldGeneral(Side side) {
        super(side);
    }

    @Override
    protected String getImageAbbreviationLetters() {
        return "KI";
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
