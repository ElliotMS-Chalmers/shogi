package model.pieces;

import model.Board;
import util.Pos;
import util.Side;

import java.util.ArrayList;

public class King extends Piece {
    private final int[][] moves = {{-1,1}, {0,1}, {1,1}, {-1,0},{1,0},{-1,-1},{0,-1},{1,-1}};

    public King(Side side) {
        super(side);
    }

//    @Override
//    protected String getImageAbbreviationLetters() {
//        return "OU";
//    }

    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        for (int[] ints : moves) {
            int availableCol = pos.col() + ints[0];
            int availableRow = pos.row() + ints[1];
            if (checkLegalMove(new Pos(availableRow,availableCol), board) != null) {
                availableMoves.add(new Pos(availableRow, availableCol));
            }
        }
        return availableMoves;
    }
}
