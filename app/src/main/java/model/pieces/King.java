package model.pieces;

import util.Side;

public class King extends Piece {
    private int[][] moves = {{-1,1}, {0,1}, {1,1}, {-1,0},{1,0},{-1,-1},{0,-1},{1,-1}};

    public King(Side side) {
        super(side);
    }
}
