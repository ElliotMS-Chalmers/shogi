package model.variants;

import com.fasterxml.jackson.annotation.JsonValue;
import model.settings.PieceSetType;
import model.pieces.Piece;
import model.Sfen;
import util.Pos;
import util.Side;

import java.util.List;
import java.util.Map;

/**
 * The `Variant` class serves as the base class for different variants of the Shogi game.
 * Subclasses should define variant-specific piece sets, promotion zones, and other variant-specific logic.
 */
public abstract class Variant {

    /**
     * The width of the board in the variant.
     * This value can vary depending on the variant (e.g., standard Shogi has a 9x9 board).
     */
    protected int width;

    /**
     * The height of the board in the variant.
     * Similar to width, the height may differ between different Shogi variants.
     */
    protected int height;

    /**
     * The starting position of the board in SFEN notation.
     * This string defines the initial placement of pieces on the board when the game begins.
     */
    protected Sfen startSfen;

    /**
     * The rule set specific to this variant.
     * A rule set includes the rules governing how the game is played, such as piece movement and promotion rules.
     */
    protected RuleSet ruleSet;

    /**
     * Returns the width of the board for this variant.
     *
     * @return the width of the board.
     */
    public int getWidth(){
        return width;
    }

    /**
     * Returns the height of the board for this variant.
     *
     * @return the height of the board.
     */
    public int getHeight(){
        return height;
    }

    /**
     * Returns the starting SFEN (Shogi Forsyth-Edwards Notation) string for this variant.
     * The SFEN string represents the initial board state in a standard notation used in Shogi.
     *
     * @return the starting SFEN string.
     */
    public Sfen getStartSfen() {
        return startSfen;
    }

    /**
     * Returns the rule set specific to this variant of the game.
     * The rule set includes the specific rules of play, such as piece movement and promotion mechanics.
     *
     * @return the rule set for the variant.
     */
    public RuleSet getRuleSet(){
        return ruleSet;
    }

    /**
     * Returns the piece set type for this variant.
     * This method is abstract, and subclasses should provide the appropriate piece set type for the variant.
     *
     * @return the piece set type for this variant.
     */
    public abstract PieceSetType getPieceSetType();

    /**
     * Returns a list of piece classes that can be part of the hand in this variant.
     * The hand typically represents pieces that have been captured and can be reintroduced into the game.
     *
     * @return a list of piece classes allowed in the hand for this variant.
     */
    public abstract List<Class<? extends Piece>> getHand();

    /**
     * Returns a map of promotion zones for each side.
     * The promotion zones define the rows where pieces can promote. The map uses the `Side` enumeration
     * to distinguish between the two sides of the board.
     *
     * @return a map of promotion zones for each side.
     */
    public abstract Map<Side, List<Integer>> getPromotionZones();

    /**
     * Determines if the given position is within the promotion zone for the specified side.
     *
     * @param pos the position to check.
     * @param side the side of the player whose promotion zone to check.
     * @return true if the position is within the promotion zone, false otherwise.
     */
    public boolean isInPromotionZone(Pos pos, Side side) {
        List<Integer> zoneRows = getPromotionZones().get(side);
        return zoneRows != null && zoneRows.contains(pos.row());
    }

    /**
     * Serializes this variant to a string representation.
     * The string is the simple name of the class (e.g., "StandardVariant" or "VariantX").
     *
     * @return a string representing this variant class.
     */
    @JsonValue
    public String serialize() {
        return getClass().getSimpleName();
    }
}
