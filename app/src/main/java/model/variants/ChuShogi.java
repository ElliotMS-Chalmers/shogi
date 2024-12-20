package model.variants;

import model.settings.PieceSetType;
import model.pieces.*;
import model.Sfen;
import util.Side;

import java.util.List;
import java.util.Map;

/**
 * Represents the Chu Shogi variant of the game.
 * Chu Shogi is a large variant of Shogi, played on a 12x12 board with a unique set of pieces and promotion zones.
 * (This is not currently incomplete and unused)
 */
public class ChuShogi extends Variant {

    /**
     * SFEN string representing the starting position of the Chu Shogi game.
     * The SFEN string includes information about the initial piece setup and other game details.
     */
    private final Sfen startSfen = new Sfen("lfcsgekgscfl/a1b1txot1b1a/mvrhdqndhrvm/pppppppppppp/3i4i3/12/12/3I4I3/PPPPPPPPPPPP/MVRHDNQDHRVM/A1B1TOXT1B1A/LFCSGKEGSCFL b - 1");

    /**
     * A map representing the promotion zones for each side in Chu Shogi.
     * The keys represent the sides (SENTE or GOTE), and the values represent the ranks where pieces can promote.
     */
    protected Map<Side, List<Integer>> promotionZones = Map.ofEntries(
            Map.entry(Side.SENTE, List.of(8, 9, 10, 11)),
            Map.entry(Side.GOTE, List.of(0, 1, 2, 3))
    );

    /**
     * Constructs a Chu Shogi game variant with a 12x12 board and the standard Chu Shogi rule set.
     */
    public ChuShogi(){
        width = 12;
        height = 12;
        ruleSet = new ShogiRuleSet();
    }

    /**
     * Gets the starting SFEN string that represents the initial state of the board.
     *
     * @return the starting SFEN string for Chu Shogi.
     */
    public Sfen getStartSfen() {
        return startSfen;
    }

    /**
     * Returns an empty list as there are no pieces in hand at the start of Chu Shogi.
     *
     * @return an empty list, as no pieces are held in hand in Chu Shogi.
     */
    public List<Class<? extends Piece>> getHand() {
        return List.of();
    }

    /**
     * Returns the piece set type for Chu Shogi.
     * The piece set used in Chu Shogi is specific to this variant.
     *
     * @return the piece set type for Chu Shogi.
     */
    @Override
    public PieceSetType getPieceSetType() {
        return PieceSetType.CHU;
    }

    /**
     * Gets the promotion zones for each side in Chu Shogi.
     * The promotion zones specify the ranks where pieces can be promoted.
     *
     * @return a map of promotion zones for both sides in Chu Shogi.
     */
    public Map<Side, List<Integer>> getPromotionZones() {
        return promotionZones;
    }
}
