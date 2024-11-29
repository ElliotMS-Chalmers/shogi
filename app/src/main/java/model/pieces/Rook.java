package model.pieces;

import util.Side;

public class Rook extends Promotable{
    private int[][] moves = {{0,1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},{0,8},{0,-1},{0,-2},{0,-3},{0,-4},{0,-5},{0,-6},{0,-7},{0,-8},{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},{8,0},{-1,0},{-2,0},{-3,0},{-4,0},{-5,0},{-6,0},{-7,0},{-8,0}};
    public Rook(Side side) {
        super(side);
    }
}
