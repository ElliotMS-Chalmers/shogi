package model.pieces;

import util.Side;

public class Pawn extends Promotable{
    private int[][] moves = {{0,1}, {0,-1}};

    public Pawn(Side side) {
        super(side);
    }
}
