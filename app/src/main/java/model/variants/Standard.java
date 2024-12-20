package model.variants;

import model.settings.PieceSetType;
import model.pieces.*;
import model.Sfen;
import util.Side;

import java.util.List;
import java.util.Map;

/**
 * The `Standard` class represents the standard Shogi game variant, following the traditional Shogi rules.
 */
public class Standard extends Variant {

    /**
     * The starting position of the board in SFEN notation for the standard Shogi variant.
     * The SFEN string represents the initial board state with all pieces in their starting positions.
     */
    private final Sfen startSfen = new Sfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1");

    /**
     * The list of pieces that can be captured and added to the hand in the standard Shogi variant.
     * This list defines the types of pieces that are allowed to be captured and reused by the player.
     */
    private final List<Class<? extends Piece>> hand = List.of(
            Pawn.class,
            Lance.class,
            Knight.class,
            SilverGeneral.class,
            GoldGeneral.class,
            Bishop.class,
            Rook.class
    );

    /**
     * The promotion zones for both sides in the standard Shogi variant.
     */
    protected Map<Side, List<Integer>> promotionZones = Map.ofEntries(
            Map.entry(Side.SENTE, List.of(6, 7, 8)),
            Map.entry(Side.GOTE, List.of(0, 1, 2))
    );

    /**
     * Constructs a `Standard` variant with default board dimensions and rule set.
     * The board dimensions are set to 9x9, and the rule set is initialized with the standard Shogi rules.
     */
    public Standard() {
        width = 9;
        height = 9;
        ruleSet = new ShogiRuleSet();
    }

    /**
     * Returns the starting SFEN string for the standard Shogi variant.
     *
     * @return the SFEN string representing the starting board position.
     */
    public Sfen getStartSfen() {
        return startSfen;
    }

    /**
     * Returns a list of piece classes that can be part of the hand in the standard Shogi variant.
     * The hand includes pieces that are captured and can be reintroduced into the game.
     *
     * @return a list of piece classes allowed in the hand for the standard variant.
     */
    public List<Class<? extends Piece>> getHand() {
        return hand;
    }

    /**
     * Returns the piece set type for the standard Shogi variant.
     *
     * @return the piece set type, which is `PieceSetType.STANDARD` for this variant.
     */
    public PieceSetType getPieceSetType() {
        return PieceSetType.STANDARD;
    }

    /**
     * Returns the promotion zones for both sides in the standard Shogi variant.
     * The promotion zones are defined as rows where pieces can be promoted during the game.
     *
     * @return a map representing the promotion zones for both sides (Sente and Gote).
     */
    public Map<Side, List<Integer>> getPromotionZones() {
        return promotionZones;
    }
}
