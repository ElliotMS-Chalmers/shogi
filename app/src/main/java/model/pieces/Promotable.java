package model.pieces;

import util.Side;

/**
 * Abstract class representing a promotable Shogi piece.
 *
 * The `Promotable` class is a subclass of `Piece` and adds the functionality to handle piece promotion.
 * In Shogi, some pieces can be promoted when they reach certain areas of the board. This class includes
 * methods for promoting the piece and checking if it is promoted.
 */
public abstract class Promotable extends Piece {
    /**
     * Indicates whether the piece is promoted
     */
    protected boolean isPromoted = false;

    /**
     * Constructs a new promotable piece with the specified side.
     *
     * @param side the side of the piece (SENTE or GOTE).
     */
    public Promotable(Side side) {
        super(side);
    }

    /**
     * Returns the SFEN abbreviation for this piece, including a '+' symbol if the piece is promoted.
     *
     * The SFEN abbreviation is a standard notation for representing Shogi pieces. If the piece is promoted,
     * a '+' is prefixed to the abbreviation (e.g., "+R" for promoted Rook, "+P" for promoted Pawn).
     *
     * @return the SFEN abbreviation of the piece, with promotion indicated if applicable.
     */
    @Override
    public String getSfenAbbreviation() {
        // Get the standard SFEN abbreviation from the superclass
        String abbr = super.getSfenAbbreviation();
        // Add '+' if the piece is promoted
        return isPromoted ? '+' + abbr : abbr;
    }

    /**
     * Promotes the piece, making it a promoted version of the piece.
     *
     * Promoting a piece changes its abilities and movement rules. This method marks the piece as promoted.
     */
    public void promote() {
        isPromoted = true;
    }

    /**
     * Returns whether the piece is promoted.
     *
     * @return true if the piece is promoted, false otherwise.
     */
    public boolean getIsPromoted() {
        return isPromoted;
    }
}
