package model.variants;

import model.pieces.*;
import model.Sfen;

import java.util.List;

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
}
