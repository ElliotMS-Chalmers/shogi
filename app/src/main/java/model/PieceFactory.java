package model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import model.pieces.*;
import model.pieces.Piece;
import util.Side;

public class PieceFactory {
    public static Piece fromSfenAbbreviation(String sfen) {
        boolean isPromoted = sfen.startsWith("+");
        char letter = sfen.charAt(sfen.length() - 1);
        Side side = Character.isUpperCase(letter) ? Side.SENTE : Side.GOTE;
        letter = Character.toLowerCase(letter);

        Piece piece;
        switch (letter) {
            case 'k':
                piece = new King(side); break;
            case 'r':
                piece = new Rook(side); break;
            case 'b':
                piece = new Bishop(side); break;
            case 'g':
                piece = new GoldGeneral(side); break;
            case 's':
                piece = new SilverGeneral(side); break;
            case 'n':
                piece = new Knight(side); break;
            case 'l':
                piece = new Lance(side); break;
            case 'p':
                piece = new Pawn(side); break;
            default:
                throw new IllegalArgumentException("Invalid sfen abbreviation: " + sfen);
        }
        if (isPromoted && piece instanceof Promotable) {
            ((Promotable) piece).promote();
        }
        return piece;
    }

    public static Piece fromClass(Class<? extends Piece> pieceClass, Side side) {
        try {
            return pieceClass.getDeclaredConstructor(Side.class).newInstance(side);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate piece: " + pieceClass.getName(), e);
        }
    }
}

