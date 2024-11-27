package model.pieces;

import util.Side;

public abstract class Promotable extends Piece {
    private boolean isPromoted = false;

    public Promotable(Side side) {
        super(side);
    }

    public void promote() {
        isPromoted = true;
    };
}
