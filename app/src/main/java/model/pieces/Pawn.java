package model.pieces;

import util.Side;
import util.Pos;

public class Pawn extends Promotable {
    private int[][] moves = {{0,1}, {0,-1}};

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

    //@Override
    //public int[][] getAvailableMoves(Pos pos, Side side){
       // int[][] availableMoves = {};
        //return availableMoves;

    //}
}
