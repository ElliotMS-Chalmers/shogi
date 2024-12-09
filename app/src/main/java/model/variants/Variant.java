package model.variants;

import model.PieceSet;
import model.PieceSetType;
import model.pieces.Piece;
import model.Sfen;
import util.Side;

import java.util.List;

public abstract class Variant {
    protected int width;
    protected int height;
    protected Sfen startSfen;
    protected RuleSet ruleSet;

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Sfen getStartSfen() { return startSfen; }

    public RuleSet getRuleSet(){return ruleSet;}

    public abstract PieceSetType getPieceSetType();

    public abstract List<Class<? extends Piece>> getHand();

}
