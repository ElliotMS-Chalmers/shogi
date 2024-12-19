package model.pieces;

import model.Board;
import model.variants.Variant;
import util.Pos;
import util.Side;

import java.util.ArrayList;

public class GoldGeneral extends Piece {
    private final int[][] moves = {{-1,1},{-1,-1}, {0,1},{0,-1}, {1,1},{1,-1}, {-1,0},{-1,0} ,{1,0},{1,0}, {0,-1},{0,1}};

    public GoldGeneral(Side side) {
        super(side);
    }

//    @Override
//    protected String getImageAbbreviationLetters() {
//        return "KI";
//    }


    @Override
    public ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board, Variant variant) {
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int team = 0;
        if (side == Side.SENTE){
            team = 1;
        }
        for (int i = 0; i < (moves.length/2); i ++) {
            int availableCol = pos.col() + moves[i*2 + team][0];
            int availableRow = pos.row() + moves[i*2 + team][1];
            if (checkLegalMoveWithinBounds(new Pos(availableRow,availableCol), board, variant)) {
                availableMoves.add(new Pos(availableRow, availableCol));
            }
        }
        return availableMoves;
    }

    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board, Variant variant){
        ArrayList<Pos> availableMoves = new ArrayList<>();
        ArrayList<Pos> availableMovesBackend = getAvailableMovesBackend(pos, board, variant);
        for (Pos availableMoveBackend : availableMovesBackend) {
            if (checkLegalMoveNotCapturingOwnPiece(new Pos(availableMoveBackend.row(),availableMoveBackend.col()), board, variant)) {
                availableMoves.add(new Pos(availableMoveBackend.row(),availableMoveBackend.col()));
            }
        }
        return availableMoves;
    }

}
