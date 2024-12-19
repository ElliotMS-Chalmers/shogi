package model.variants;

import model.settings.PieceSetType;
import model.pieces.*;
import model.Sfen;
import util.Side;

import java.util.List;
import java.util.Map;

public class Standard extends Variant {
    private final Sfen startSfen = new Sfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1");
    private final List<Class<? extends Piece>> hand = List.of(
            Pawn.class,
            Lance.class,
            Knight.class,
            SilverGeneral.class,
            GoldGeneral.class,
            Bishop.class,
            Rook.class
    );
    protected Map<Side, List<Integer>> promotionZones = Map.ofEntries(
            Map.entry(Side.SENTE, List.of(6, 7, 8)),
            Map.entry(Side.GOTE, List.of(0, 1, 2))
    );

    public Standard(){
        width = 9;
        height = 9;
        ruleSet = new ShogiRuleSet();
    }

    public Sfen getStartSfen() {
        return startSfen;
    }

    public List<Class<? extends Piece>> getHand() {
        return hand;
    }

    public PieceSetType getPieceSetType() {
        return PieceSetType.STANDARD;
    }

    public Map<Side, List<Integer>> getPromotionZones() {
        return promotionZones;
    }
}
