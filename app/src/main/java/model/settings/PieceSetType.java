package model.settings;

/**
 * Enum representing the types of piece sets available for a Shogi game.
 * Each type corresponds to a specific set of pieces able to be used by different variants.
 * (Currently STANDARD is the only PieceSetType used but everything is set up to support variants using other pieces)
 */
public enum PieceSetType {
    /**
     * The standard piece set for traditional Shogi.
     */
    STANDARD,

    /**
     * The Chu Shogi piece set, used for the Chu Shogi variant.
     */
    CHU,

    /**
     * The Kyo Shogi piece set, used for the Kyo Shogi variant.
     */
    KYO
}

