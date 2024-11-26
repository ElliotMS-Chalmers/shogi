package view;

import util.Settings;

public class Theme {
    PieceSet pieceSet;
    BoardTheme boardTheme;

    public Theme(PieceSet pieceSet, BoardTheme boardTheme) {
        this.pieceSet = pieceSet;
        this.boardTheme = boardTheme;
    }

    public PieceSet getPieceSet() {
        return pieceSet;
    }

    public void setPieceSet(PieceSet pieceSet) {
        this.pieceSet = pieceSet;
    }

    public BoardTheme getBoardTheme() {
        return boardTheme;
    }

    public void setBoardTheme(BoardTheme boardTheme) {
        this.boardTheme = boardTheme;
    }
}
