package model.pieces;

import util.Side;

public class GoldGeneral extends Piece {
    private int[][] moves = {{-1,1}, {0,1}, {1,1}, {-1,0},{1,0},{0,-1}};

    public GoldGeneral(Side side) {
        super(side);
    }

    @Override
    protected String getImageAbbreviationLetters() {
        return "KI";
    }

}
