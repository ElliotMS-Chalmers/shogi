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
        char abbr = '.';
        switch(side){
            case GOTE: abbr = letter; break;
            case SENTE: abbr = Character.toLowerCase(letter);
        }
        return abbr;
    }
}
