package model.pieces;

import model.Board;
import model.variants.Variant;
import util.Pos;
import util.Side;

import java.util.ArrayList;

public class SilverGeneral extends Promotable {
    private final int[][] moves = {{-1,1},{-1,-1}, {0,1},{0,-1}, {1,1},{1,-1}, {-1,-1},{-1,1}, {1,-1},{1,1}};
    private final int[][] promotedMoves = {{-1,1},{-1,-1}, {0,1},{0,-1}, {1,1},{1,-1}, {-1,0},{-1,0} ,{1,0},{1,0}, {0,-1},{0,1}};

    public SilverGeneral(Side side) {
        super(side);
    }

//    @Override
//    protected String getImageAbbreviationLetters() {
//        return "GI";
//    }
//
//    @Override
//    protected String getPromotedImageAbbreviationLetters() {
//        return "NG";
//    }

    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board, Variant variant){
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int team = 0;
        int availableCol;
        int availableRow;
        int movesLength;
        if (side == Side.SENTE){
            team = 1;
        }
        if (isPromoted){
            movesLength = promotedMoves.length;
        } else {
            movesLength = moves.length;
        }
        for (int i = 0; i < (movesLength/2); i ++) {
            if (isPromoted){
                availableCol = pos.col() + promotedMoves[i*2 + team][0];
                availableRow = pos.row() + promotedMoves[i*2 + team][1];
            } else {
                availableCol = pos.col() + moves[i * 2 + team][0];
                availableRow = pos.row() + moves[i * 2 + team][1];
            }
            if (checkLegalMove(new Pos(availableRow,availableCol), board, variant) != null) {
                availableMoves.add(new Pos(availableRow, availableCol));
            }
        }
        return availableMoves;
    }
}
