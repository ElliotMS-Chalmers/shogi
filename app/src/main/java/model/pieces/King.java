package model.pieces;

import util.Pos;
import util.Side;

import java.util.ArrayList;

public class King extends Piece {
    private final int[][] moves = {{-1,1}, {0,1}, {1,1}, {-1,0},{1,0},{-1,-1},{0,-1},{1,-1}};

    public King(Side side) {
        super(side);
    }

    @Override
    protected String getImageAbbreviationLetters() {
        return "OU";
    }

    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        for (int[] ints : moves) {
            int availableCol = pos.col() + ints[0];
            int availableRow = pos.row() + ints[1];
            if (availableCol >= 0 && availableCol <= 8 && availableRow >= 0 && availableRow <= 8) {
                availableMoves.add(new Pos(availableRow,availableCol));
            }
        }
        return availableMoves;
    }
}
