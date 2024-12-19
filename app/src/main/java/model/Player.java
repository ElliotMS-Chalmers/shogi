package model;

import model.pieces.Piece;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import util.Side;
import java.lang.reflect.Constructor;

/**
 * Represents a player in the game, maintaining their side and captured pieces.
 */
public class Player {
    private Side side;
    private Map<Class<? extends Piece>, Integer> capturedPieces = new LinkedHashMap<>() {};

    /**
     * Constructs a new Player instance with the specified side.
     *
     * @param side the side of the player (e.g., black or white).
     */
    public Player(Side side) {
        this.side = side;
    }

    /**
     * Returns the player's captured pieces in SFEN format.
     *
     * @return a string representing the captured pieces in SFEN format.
     */
    public String getHandAsSfen() {
        StringBuilder sfen = new StringBuilder();
        capturedPieces.forEach((pieceClass, count) -> {
            try {
                Constructor<?> constructor = pieceClass.getConstructor(Side.class);
                Piece piece = (Piece) constructor.newInstance(side);
                String abbr = piece.getSfenAbbreviation();
                if (count > 1) {
                    sfen.append(count).append(abbr);
                } else if (count == 1) {
                    sfen.append(abbr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return sfen.toString();
    }

    /**
     * Adds a specified amount (positive or negative) of a captured piece to the player's hand.
     *
     * @param pieceClass the class of the piece to be added.
     * @param amount     the amount of the piece to add.
     */
    public void addCapturedPiece(Class<? extends Piece> pieceClass, int amount) {
        if (capturedPieces.get(pieceClass) != null) {
            capturedPieces.put(pieceClass, capturedPieces.get(pieceClass) + amount);
        }
    }

    /**
     * Adds a single captured piece to the player's hand.
     *
     * @param pieceClass the class of the piece to be added.
     */
    public void addCapturedPiece(Class<? extends Piece> pieceClass) {
        addCapturedPiece(pieceClass, 1);
    }

    /**
     * Removes a single captured piece from the player's hand.
     *
     * @param pieceClass the class of the piece to be removed.
     */
    public void removeCapturedPiece(Class<? extends Piece> pieceClass) {
        addCapturedPiece(pieceClass, -1);
    }

    /**
     * Initializes the player's hand with a list of piece classes, setting their counts to zero.
     *
     * @param hand a list of piece classes to initialize in the player's hand.
     */
    public void intializeHand(List<Class<? extends Piece>> hand) {
        for (Class<? extends Piece> pieceClass : hand) {
            capturedPieces.put(pieceClass, 0);
        }
    }
}