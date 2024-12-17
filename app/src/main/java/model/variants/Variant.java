package model.variants;

import model.settings.PieceSetType;
import model.pieces.Piece;
import model.Sfen;
import util.Pos;
import util.Side;

import java.util.List;
import java.util.Map;

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

    public abstract Map<Side, List<Integer>> getPromotionZones();

    public boolean inPromotionZone(Pos pos, Side side) {
        List<Integer> zoneRows = getPromotionZones().get(side);
        return zoneRows != null && zoneRows.contains(pos.row());
    }

}
