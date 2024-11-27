package view;

import util.Settings;

public class Theme {
    private final PieceSet pieceSet;
    private final BoardTheme boardTheme;

    public Theme(PieceSet pieceSet, BoardTheme boardTheme) {
        this.pieceSet = pieceSet;
        this.boardTheme = boardTheme;
    }

    public PieceSet getPieceSet() {
        return pieceSet;
    }

    public BoardTheme getBoardTheme() {
        return boardTheme;
    }
}
