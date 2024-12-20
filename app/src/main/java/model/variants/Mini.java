package model.variants;

import model.settings.PieceSetType;
import model.pieces.*;
import model.Sfen;
import util.Side;

import java.util.List;
import java.util.Map;

/**
 * The `Mini` class represents a variant of Shogi played on a 5x5 board with a limited set of pieces and promotion zones.
 */
public class Mini extends Variant {

    /** The starting position of the game in SFEN format. */
    private final Sfen startSfen = new Sfen("rbsgk/4p/5/P4/KGSBR b - 1");

    /** The list of pieces available in the player's hand for the Mini variant. */
    private final List<Class<? extends Piece>> hand = List.of(
            Pawn.class,
            SilverGeneral.class,
            GoldGeneral.class,
            Bishop.class,
            Rook.class
    );

    /** The promotion zones for both sides in the Mini variant. */
    protected Map<Side, List<Integer>> promotionZones = Map.ofEntries(
            Map.entry(Side.SENTE, List.of(4)),
            Map.entry(Side.GOTE, List.of(0))
    );

    /**
     * Constructs a new Mini variant with a 5x5 board and the standard rule set.
     */
    public Mini(){
        width = 5;
        height = 5;
        ruleSet = new ShogiRuleSet();
    }

    /**
     * Returns the starting position of the game in SFEN format.
     *
     * @return the starting SFEN position
     */
    public Sfen getStartSfen() {
        return startSfen;
    }

    /**
     * Returns the list of pieces available in the player's hand for the Mini variant.
     *
     * @return the list of piece classes
     */
    public List<Class<? extends Piece>> getHand() {
        return hand;
    }

    /**
     * Returns the type of piece set used in the Mini variant.
     *
     * @return the piece set type (STANDARD)
     */
    @Override
    public PieceSetType getPieceSetType() {
        return PieceSetType.STANDARD;
    }

    /**
     * Returns the promotion zones for both sides in the Mini variant.
     * The promotion zones define the rows where pieces can be promoted.
     *
     * @return a map of promotion zones for each side
     */
    public Map<Side, List<Integer>> getPromotionZones() {
        return promotionZones;
    }
}
