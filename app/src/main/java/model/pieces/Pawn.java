package model.pieces;

import util.Side;
import util.Pos;

import java.awt.*;
import java.util.ArrayList;

public class Pawn extends Promotable {
    private final int[][] moves = {{0,1}, {0,-1}};
    private final int[][] promotedMoves = {{-1,1},{-1,-1}, {0,1},{0,-1}, {1,1},{1,-1}, {-1,0},{-1,0} ,{1,0},{1,0}, {0,-1},{0,1}};

    public Pawn(Side side) {
        super(side);
    }

    @Override
    protected String getImageAbbreviationLetters() {
        return "FU";
    }

    @Override
    protected String getPromotedImageAbbreviationLetters() {
        return "TO";
    }

    @Override
    public ArrayList<ArrayList<Integer>> getAvailableMoves(Pos pos, Side side){
        ArrayList<ArrayList<Integer>> availableMoves = new ArrayList<>();
        int team = 0;
        int availableCol;
        int availableRow;
        int movesLength;
        if (side == Side.SENTE){
            team = 1;
        }
        if (this.getIsPromoted()){
            movesLength = promotedMoves.length;
        } else {
            movesLength = moves.length;
        }

        for (int i = 0; i < (movesLength/2); i ++) {
            if (this.getIsPromoted()){
                availableCol = pos.col() + promotedMoves[i * 2 + team][0];
                availableRow = pos.row() + promotedMoves[i * 2 + team][1];
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
