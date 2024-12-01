package model.pieces;

import util.Side;

public abstract class Piece {
    protected Side side;

    public Piece(Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }

    public char getSfenAbbreviation(){
        char letter = this.getClass().getSimpleName().charAt(0);
        if (letter == 'K') {
            letter = this.getClass().getSimpleName().equals("King") ? 'K' : 'N';
        }
        return switch(side){
            case SENTE -> letter;
            case GOTE -> Character.toLowerCase(letter);
        };
    }

    protected abstract String getImageAbbreviationLetters();

    public String getImageAbbreviation() {
        return switch (side) {
            case GOTE -> "1" + getImageAbbreviationLetters();
            case SENTE -> "0" + getImageAbbreviationLetters();
        };
    };
}
