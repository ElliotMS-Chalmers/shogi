package model.pieces;

import util.Side;

public class Knight extends Promotable {
    private int[][] moves = {{-1,2}, {-1,-2},{1,2}, {1,-2}};

    public Knight(Side side) {
        super(side);
    }

    @Override
    protected String getImageAbbreviationLetters() {
        return "KE";
    }

    @Override
    protected String getPromotedImageAbbreviationLetters() {
        return "NK";
    }
}
