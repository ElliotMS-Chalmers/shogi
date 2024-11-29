package model.pieces;

import util.Side;

public class Bishop extends Promotable {
    private int[][] moves = {{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7},{8,8},{-1,1},{-2,2},{-3,3},{-4,4},{-5,5},{-6,6},{-7,7},{-8,8},{-1,-1},{-2,-2},{-3,-3},{-4,-4},{-5,-5},{-6,-6},{-7,-7},{-8,-8},{1,-1},{2,-2},{3,-3},{4,-4},{5,-5},{6,-6},{7,-7},{8,-8}};
    public Bishop(Side side) {
        super(side);
    }
}
