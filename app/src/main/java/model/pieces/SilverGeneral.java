package model.pieces;

import util.Side;

public class SilverGeneral extends Promotable{
    private int[][] moves = {{-1,1}, {0,1}, {1,1},{-1,-1},{1,-1}};

    public SilverGeneral(Side side) {
        super(side);
    }
}
