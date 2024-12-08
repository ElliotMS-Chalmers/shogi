package model;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.scene.image.Image;

public class ThemeManager {
    private Map<String, BoardTheme> boardThemes;
    private Map<String, PieceSet> pieceSets;
    private Map<String, SoundSet> soundSets;

    public ThemeManager() {
        loadBoardThemes();
        loadPieceSets();
        loadSoundSets();
    }

    private void loadBoardThemes() {
        this.boardThemes = JsonLoader.load("/board_themes.json", new TypeReference<Map<String, BoardTheme>>() {});
    }

    private void loadPieceSets() {
        this.pieceSets = JsonLoader.load("/piece_sets.json", new TypeReference<Map<String, PieceSet>>() {});
    }

    private void loadSoundSets() {
        this.soundSets = JsonLoader.load("/sound_sets.json", new TypeReference<Map<String, SoundSet>>() {});
    }

    public Map<String, BoardTheme> getBoardThemes() {
        return boardThemes;
    }

    public Map<String, PieceSet> getPieceSets() {
        return pieceSets;
    }

    public Map<String, SoundSet> getSoundSets() {
        return soundSets;
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

    public SoundSet getSoundSet(String name) {
        SoundSet soundSet = soundSets.get(name);
        if (soundSet == null) { throw new IllegalArgumentException(String.format("Sound set %s not found.", name)); }
        return soundSet;
    }

    public String getBoardThemeName(BoardTheme boardTheme) {
        return getKeyFromValue(boardThemes, boardTheme);
    }

    public String getPieceSetName(PieceSet pieceSet) {
        return getKeyFromValue(pieceSets, pieceSet);
    }

    private <T> String getKeyFromValue(Map<String, T> map, T value) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null); // Handle case where value is not found
    }

}