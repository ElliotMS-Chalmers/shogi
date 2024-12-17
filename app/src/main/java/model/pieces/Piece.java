package model.pieces;

import model.Board;
import model.variants.Variant;
import util.Side;
import util.Pos;

import java.util.ArrayList;

/**
 * Abstract class representing a piece
 */
public abstract class Piece {
    protected Side side;

    public Piece(Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }

    public String getSfenAbbreviation(){
        char letter = this.getClass().getSimpleName().charAt(0);
        return switch(side){
            case SENTE -> String.valueOf(letter);
            case GOTE -> String.valueOf(letter).toLowerCase();
        };
    }

//    protected abstract String getImageAbbreviationLetters();

    public abstract ArrayList<Pos> getAvailableMoves(Pos pos, Board board, Variant variant);

    public abstract ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board, Variant variant);


//    public String getImageAbbreviation() {
//        return switch (side) {
//            case GOTE -> "1" + getImageAbbreviationLetters();
//            case SENTE -> "0" + getImageAbbreviationLetters();
//        };
//    };



    public boolean checkLegalMoveWithinBounds (Pos pos, Board board, Variant variant){
        return pos.col() >= 0 && pos.col() <= (variant.getWidth() - 1) && pos.row() >= 0 && pos.row() <= (variant.getHeight() - 1);
    }

    public boolean checkLegalMoveNotCapturingOwnPiece (Pos pos, Board board, Variant variant){
        if (board.getPieceAt(pos) != null) {
            return board.getPieceAt(pos).getSide() != side;
        }
        return true;
    }

    public Pos checkLegalMove (Pos pos, Board board, Variant variant){
        boolean valid = true;
        if (pos.col() >= 0 && pos.col() <= (variant.getWidth()-1) && pos.row() >= 0 && pos.row() <= (variant.getHeight()-1)) {
            if (board.getPieceAt(pos) != null) {
                if (board.getPieceAt(pos).getSide() == side) {
                    valid = false;
                }
            }
            if (valid) {
                return pos;
            }
        }
        return null;
    }

}
