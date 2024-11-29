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
        abbr = switch (side) {
            case GOTE -> "0" + abbr;
            case SENTE -> "1" + abbr;
        };
        return abbr;
    }

    protected abstract String getPromotedImageAbbreviationLetters();

    public void promote() {
        isPromoted = true;
    };
}
