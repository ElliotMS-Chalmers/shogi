package model.pieces;

import util.Side;
import util.Pos;

import java.util.ArrayList;

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
        if (letter == 'K') {
            letter = this.getClass().getSimpleName().equals("King") ? 'K' : 'N';
        }
        return switch(side){
            case SENTE -> String.valueOf(letter);
            case GOTE -> String.valueOf(letter).toLowerCase();
        };
    }

    protected abstract String getImageAbbreviationLetters();

    public abstract ArrayList<ArrayList<Integer>> getAvailableMoves(Pos pos);

    public String getImageAbbreviation() {
        return switch (side) {
            case GOTE -> "1" + getImageAbbreviationLetters();
            case SENTE -> "0" + getImageAbbreviationLetters();
        };
    };
}
