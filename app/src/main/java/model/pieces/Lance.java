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
    public ArrayList<Pos> getAvailableMoves(Pos pos){
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int team = 0;
        int availableCol;
        int availableRow;
        int movesLength;
        if (side == Side.SENTE){
            team = 1;
        }
        if (isPromoted){
            movesLength = promotedMoves.length;
        } else {
            movesLength = moves.length;
        }
        for (int i = 0; i < (movesLength/2); i ++) {
            if (isPromoted){
                availableCol = pos.col() + promotedMoves[i*2 + team][0];
                availableRow = pos.row() + promotedMoves[i*2 + team][1];
            } else {
                availableCol = pos.col() + moves[i * 2 + team][0];
                availableRow = pos.row() + moves[i * 2 + team][1];
            }
            if (availableCol >= 0 && availableCol <= 8 && availableRow >= 0 && availableRow <= 8){
                availableMoves.add(new Pos(availableRow,availableCol));
            }
        }
        return availableMoves;
    }
}
