package model.pieces;

import model.Board;
import model.variants.Variant;
import util.Pos;
import util.Side;

import java.util.ArrayList;

public class Lance extends Promotable {
    private final int[][] promotedMoves = {{-1,1},{-1,-1}, {0,1},{0,-1}, {1,1},{1,-1}, {-1,0},{-1,0} ,{1,0},{1,0}, {0,-1},{0,1}};
    public Lance(Side side) {
        super(side);
    }

//    @Override
//    protected String getImageAbbreviationLetters() { return "KY"; }
//
//    @Override
//    protected String getPromotedImageAbbreviationLetters() {
//        return "NY";
//    }

    @Override
    public ArrayList<Pos> getAvailableMoves(Pos pos, Board board){
        ArrayList<Pos> availableMoves = new ArrayList<>();
        int team = 0;
        int availableCol;
        int availableRow;
        int movesLength = board.getWidth();
        boolean previousPieceEnemy;
        if (side == Side.SENTE){
            team = 1;
        }
        if (isPromoted){
            for (int i = 0; i < (promotedMoves.length/2); i ++) {
                availableCol = pos.col() + promotedMoves[i *2 + team][0];
                availableRow = pos.row() + promotedMoves[i *2 + team][1];
                if (checkLegalMove(new Pos(availableRow,availableCol), board) != null) {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }
            }
        } else {
            previousPieceEnemy = false;
            for (int i = 1; i <= (movesLength); i ++) {
                availableCol = (pos.col());
                availableRow = (pos.row() + (i * -1* (-1+ team*2)));
                if (previousPieceEnemy){
                    break;
                }
                if (checkLegalMove(new Pos(availableRow, availableCol), board) != null) {
                    if (board.getPieceAt(new Pos(availableRow, availableCol)) != null) {
                        if (board.getPieceAt(new Pos(availableRow, availableCol)).getSide() != side) {
                            previousPieceEnemy = true;
                        }
                    }
                }
                if (checkLegalMove(new Pos(availableRow, availableCol), board) == null){
                    break;
                } else {
                    availableMoves.add(new Pos(availableRow, availableCol));
                }
            }
        }

        return availableMoves;
    }
}
