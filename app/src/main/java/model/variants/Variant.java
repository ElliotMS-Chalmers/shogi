package model.variants;

import model.pieces.Piece;
import model.Sfen;
import util.Side;

public abstract class Variant {
    protected int width;
    protected int height;
    protected Sfen startSfen;

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Sfen getStartSfen() { return startSfen; }

    public abstract Class<? extends Piece>[] getHand(Side side);
}
