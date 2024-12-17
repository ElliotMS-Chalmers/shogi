package model.variants;

import model.settings.PieceSetType;
import model.pieces.*;
import model.Sfen;
import util.Side;

import java.util.List;
import java.util.Map;

public class Mini extends Variant {
    private final Sfen startSfen = new Sfen("rbsgk/4p/5/P4/KGSBR b - 1");
    private final List<Class<? extends Piece>> hand = List.of(
            Pawn.class,
            SilverGeneral.class,
            GoldGeneral.class,
            Bishop.class,
            Rook.class
    );
    protected Map<Side, List<Integer>> promotionZones = Map.ofEntries(
            Map.entry(Side.SENTE, List.of(4)),
            Map.entry(Side.GOTE, List.of(0))
    );

    public Mini(){
        width = 5;
        height = 5;
        ruleSet = new ShogiRuleSet();
    }

    public Sfen getStartSfen() {
        return startSfen;
    }

    public List<Class<? extends Piece>> getHand() {
        return hand;
    }

    @Override
    public PieceSetType getPieceSetType() {
        return PieceSetType.STANDARD;
    }

    public Map<Side, List<Integer>> getPromotionZones() {
        return promotionZones;
    }
}
