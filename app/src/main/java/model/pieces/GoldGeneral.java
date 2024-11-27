package model.pieces;

import util.Side;

public class GoldGeneral extends Promotable {
    private int[][] moves = {{-1,1}, {0,1}, {1,1}, {-1,0},{1,0},{0,-1}};

    public GoldGeneral(Side side) {
        super(side);
    }
}
