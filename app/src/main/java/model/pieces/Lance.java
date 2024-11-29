package model.pieces;

import util.Side;

public class Lance extends Promotable {
    private int[][] moves = {{0,1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},{0,8}};
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
}
