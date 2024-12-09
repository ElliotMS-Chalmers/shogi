package model.variants;

import model.PieceSetType;
import model.pieces.*;
import model.Sfen;

import java.util.List;

public class Mini extends Variant {
    private final Sfen startSfen = new Sfen("rbsgk/4p/5/P4/KGSBR b - 1");
    private final List<Class<? extends Piece>> hand = List.of(
            Pawn.class,
            SilverGeneral.class,
            GoldGeneral.class,
            Bishop.class,
            Rook.class
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
}
