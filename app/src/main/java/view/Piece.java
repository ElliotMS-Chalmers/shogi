package view;

public enum Piece {
    SENTE_KING("K", "0OU"),
    SENTE_ROOK("R", "0HI"),
    SENTE_BISHOP("B", "0KA"),
    SENTE_GOLD_GENERAL("G", "0KI"),
    SENTE_SILVER_GENERAL("S", "0GI"),
    SENTE_KNIGHT("N", "0KE"),
    SENTE_LANCE("L", "0KY"),
    SENTE_PAWN("P", "0FU"),
    SENTE_PROMOTED_ROOK("+R", "0RY"),
    SENTE_PROMOTED_BISHOP("+B", "0UM"),
    SENTE_PROMOTED_SILVER("+S", "0NG"),
    SENTE_PROMOTED_KNIGHT("+K", "0NK"),
    SENTE_PROMOTED_LANCE("+L", "0NY"),
    SENTE_PROMOTED_PAWN("+P", "0TO"),

    GOTE_KING("k", "1OU"),
    GOTE_ROOK("r", "1HI"),
    GOTE_BISHOP("b", "1KA"),
    GOTE_GOLD_GENERAL("g", "1KI"),
    GOTE_SILVER_GENERAL("s", "1GI"),
    GOTE_KNIGHT("n", "1KE"),
    GOTE_LANCE("l", "1KY"),
    GOTE_PAWN("p", "1FU"),
    GOTE_PROMOTED_ROOK("+r", "1RY"),
    GOTE_PROMOTED_BISHOP("+b", "1UM"),
    GOTE_PROMOTED_SILVER("+s", "1NG"),
    GOTE_PROMOTED_KNIGHT("+k", "1NK"),
    GOTE_PROMOTED_LANCE("+l", "1NY"),
    GOTE_PROMOTED_PAWN("+p", "1TO");
    
    private final String sfenAbbr;
    private final String imgAbbr;

    Piece(String sfenAbbr, String imgAbbr) {
        this.sfenAbbr = sfenAbbr;
        this.imgAbbr = imgAbbr;
    }

    public static Piece fromSfenAbbreviation(String abbr) {
        for (Piece piece : values()) {
            if (piece.sfenAbbr.equals(abbr)) {
                return piece;
            }
        }
        throw new IllegalArgumentException("Invalid SFEN abbreviation");
    }

    public String getSfenAbbreviation() {
        return sfenAbbr;
    }

    public String getImageAbbreviation() {
        return imgAbbr;
    }
}
