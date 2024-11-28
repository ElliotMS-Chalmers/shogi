package model;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import util.JsonLoader;

public class ThemeManager {
    private Map<String, BoardTheme> boardThemes;
    private Map<String, PieceSet> pieceSets;

    public ThemeManager() {
        loadBoardThemes();
        loadPieceSets();
    }

    private void loadBoardThemes() {
        this.boardThemes = JsonLoader.load("/board_themes.json", new TypeReference<Map<String, BoardTheme>>() {});
    }

    private void loadPieceSets() {
        this.pieceSets = JsonLoader.load("/piece_sets.json", new TypeReference<Map<String, PieceSet>>() {});
    }

    public Map<String, BoardTheme> getBoardThemes() {
        return boardThemes;
    }

    public Map<String, PieceSet> getPieceSets() {
        return pieceSets;
    }

    public BoardTheme getBoardTheme(String name) {
        BoardTheme boardTheme = boardThemes.get(name);
        if (boardTheme == null) { throw new IllegalArgumentException(String.format("Board theme %s not found.", name)); }
        return boardTheme;
    }

    public PieceSet getPieceSet(String name) {
        PieceSet pieceSet = pieceSets.get(name);
        if (pieceSet == null) { throw new IllegalArgumentException(String.format("Piece set %s not found.", name)); }
        return pieceSet;
    }
}