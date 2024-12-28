package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

import model.pieces.Piece;
import util.Pos;


/**
 * Represents a move in the game of Shogi, including information about the position
 * of the move, the piece being moved, and any captured piece.
 *
 * @param from the starting position of the move
 * @param to the ending position of the move
 * @param movedPiece the piece being moved
 * @param capturedPiece the piece that is captured (if any)
 */
public record Move(Pos from, Pos to, Piece movedPiece, Piece capturedPiece, boolean promoted) {

    /**
     * Constructor to create a Move object.
     * This constructor is used for JSON deserialization to populate the fields.
     *
     * @param from the starting position of the move
     * @param to the ending position of the move
     * @param movedPiece the piece being moved
     * @param capturedPiece the piece that is captured (if any)
     */
    @JsonCreator
    public Move(
            @JsonProperty("from") Pos from,
            @JsonProperty("to") Pos to,
            @JsonProperty("movedPiece") Piece movedPiece,
            @JsonProperty("capturedPiece") Piece capturedPiece,
            @JsonProperty("promoted") boolean promoted
    )
    {
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.promoted = promoted;
    }

    /**
     * Checks if the move originates from the player's hand.
     *
     * @return true if the move is from the player's hand (i.e., 'from' position is null), false otherwise
     */
    public boolean fromPlayerHand() {
        return (this.from() == null);
    }

    /**
     * Converts the move to a string representation in the SFEN notation format.
     * The string format represents the moved piece, the move type ('*' for a move from hand,
     * 'x' for a capture, and '-' for a normal move), and the destination position.
     *
     * @return a string representation of the move in SFEN notation.
     */
    public String toString() {
        String abbr = movedPiece.getSfenAbbreviation();
        abbr = promoted ? abbr.substring(1) : abbr; // prevents moves where piece was promoted from displaying piece as promoted before move was made

        char moveType;
        if (from == null)
            moveType = '*';
        else if (capturedPiece != null)
            moveType = 'x';
        else
            moveType = '-';

        return abbr + moveType + (to.col() + 1) + (to.row() + 1) + (promoted ? "+" : "");
    }
}