package model.pieces;

import util.Side;

/**
 * Factory class for creating instances of Shogi pieces based on SFEN (Shogi Forsyth-Edwards Notation) abbreviations
 * or piece class types.
 */
public class PieceFactory {

    /**
     * Creates a Shogi piece based on its SFEN abbreviation.
     *
     * @param sfen the SFEN abbreviation representing a piece.
     * @return the corresponding `Piece` object.
     * @throws IllegalArgumentException if the SFEN abbreviation is invalid or unrecognized.
     */
    public static Piece fromSfenAbbreviation(String sfen) {
        // Determine if the piece is promoted by checking the first character
        boolean isPromoted = sfen.startsWith("+");

        // Extract the letter representing the piece type (convert to lowercase)
        char letter = sfen.charAt(sfen.length() - 1);

        // Determine the side of the piece based on the case of the letter
        Side side = Character.isUpperCase(letter) ? Side.SENTE : Side.GOTE;
        letter = Character.toLowerCase(letter);

        // Create the piece based on the letter and side
        // (if we had more pieces maybe we could look over all piece types and check if abbreviations match)
        Piece piece = switch (letter) {
            case 'k' -> new King(side);
            case 'r' -> new Rook(side);
            case 'b' -> new Bishop(side);
            case 'g' -> new GoldGeneral(side);
            case 's' -> new SilverGeneral(side);
            case 'n' -> new Knight(side);
            case 'l' -> new Lance(side);
            case 'p' -> new Pawn(side);
            case 'c' -> new CopperGeneral(side);
            default -> throw new IllegalArgumentException("Invalid sfen abbreviation: " + sfen);
        };


        // Promote the piece if it is marked as promoted
        if (isPromoted && piece instanceof Promotable) {
            ((Promotable) piece).promote();
        }

        return piece;
    }

    /**
     * Creates a Shogi piece instance based on the piece class and side.
     *
     * @param pieceClass the class type of the piece to create (e.g., `King.class`, `Rook.class`).
     * @param side the side of the piece (SENTE or GOTE).
     * @return the corresponding `Piece` object.
     * @throws RuntimeException if the instantiation fails due to reflection issues.
     */
    public static Piece fromClass(Class<? extends Piece> pieceClass, Side side) {
        try {
            // Create the piece instance using reflection
            return pieceClass.getDeclaredConstructor(Side.class).newInstance(side);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate piece: " + pieceClass.getName(), e);
        }
    }
}
