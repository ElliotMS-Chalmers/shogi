package util;

public enum Side {
    SENTE, // Attacker, white in traditional chess, black in shogi
    GOTE;   // Defender, black in traditional chess, white in shogi

    /**
     * Returns the opposite side.
     *
     * @return the opposite side of the current side.
     */
    public Side opposite() {
        return this == SENTE ? GOTE : SENTE;
    }
}
