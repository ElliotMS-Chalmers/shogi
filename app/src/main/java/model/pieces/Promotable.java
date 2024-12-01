package model.pieces;

import util.Side;

public abstract class Promotable extends Piece {
    private boolean isPromoted = false;

    public Promotable(Side side) {
        super(side);
    }

    @Override
    public String getImageAbbreviation() {
        String abbr = isPromoted ? getPromotedImageAbbreviationLetters() : getImageAbbreviationLetters();
        return switch (side) {
            case GOTE -> "1" + abbr;
            case SENTE -> "0" + abbr;
        };
    }

    protected abstract String getPromotedImageAbbreviationLetters();

    public void promote() {
        isPromoted = true;
    };
}
