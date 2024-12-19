package model.settings;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Manages and provides access to resources like board themes, piece sets, and sound sets.
 */
public class ResourceManager {

    /**
     * A map of available board themes, keyed by their names.
     */
    private Map<String, BoardTheme> boardThemes;

    /**
     * A map of available piece sets, categorized by their types (e.g., STANDARD, CHU, KYO).
     */
    private Map<PieceSetType, Map<String, PieceSet>> pieceSets;

    /**
     * A map of available sound sets, keyed by their names.
     */
    private Map<String, SoundSet> soundSets;

    /**
     * Initializes the resource manager and loads all resource configurations.
     */
    public ResourceManager() {
        loadBoardThemes();
        loadPieceSets();
        loadSoundSets();
    }

    /**
     * Loads board themes from the configuration file.
     */
    private void loadBoardThemes() {
        this.boardThemes = JsonLoader.loadResource("/board_themes.json",
                new TypeReference<Map<String, BoardTheme>>() {});
    }

    /**
     * Loads piece sets from the configuration file.
     */
    private void loadPieceSets() {
        this.pieceSets = JsonLoader.loadResource("/piece_sets.json",
                new TypeReference<Map<PieceSetType, Map<String, PieceSet>>>() {});
    }

    /**
     * Loads sound sets from the configuration file.
     */
    private void loadSoundSets() {
        this.soundSets = JsonLoader.loadResource("/sound_sets.json",
                new TypeReference<Map<String, SoundSet>>() {});
    }

    /**
     * Retrieves the map of board themes.
     *
     * @return A map of board themes.
     */
    public Map<String, BoardTheme> getBoardThemes() {
        return boardThemes;
    }

    /**
     * Retrieves the map of piece sets categorized by type.
     *
     * @return A map of piece sets.
     */
    public Map<PieceSetType, Map<String, PieceSet>> getPieceSets() {
        return pieceSets;
    }

    /**
     * Retrieves the map of sound sets.
     *
     * @return A map of sound sets.
     */
    public Map<String, SoundSet> getSoundSets() {
        return soundSets;
    }

    /**
     * Retrieves a specific board theme by its name.
     *
     * @param name The name of the board theme.
     * @return The corresponding {@code BoardTheme}.
     * @throws IllegalArgumentException if the board theme is not found.
     */
    public BoardTheme getBoardTheme(String name) {
        BoardTheme boardTheme = boardThemes.get(name);
        if (boardTheme == null) {
            throw new IllegalArgumentException(String.format("Board theme %s not found.", name));
        }
        return boardTheme;
    }

    /**
     * Retrieves a specific piece set by its type and name.
     *
     * @param type The type of the piece set.
     * @param name The name of the piece set.
     * @return The corresponding {@code PieceSet}.
     * @throws IllegalArgumentException if the piece set is not found.
     */
    public PieceSet getPieceSet(PieceSetType type, String name) {
        PieceSet pieceSet = pieceSets.get(type).get(name);
        if (pieceSet == null) {
            throw new IllegalArgumentException(String.format("Piece set %s not found.", name));
        }
        return pieceSet;
    }

    /**
     * Retrieves a specific sound set by its name.
     *
     * @param name The name of the sound set.
     * @return The corresponding {@code SoundSet}.
     * @throws IllegalArgumentException if the sound set is not found.
     */
    public SoundSet getSoundSet(String name) {
        SoundSet soundSet = soundSets.get(name);
        if (soundSet == null) {
            throw new IllegalArgumentException(String.format("Sound set %s not found.", name));
        }
        return soundSet;
    }

    /**
     * Retrieves the name of a specific board theme.
     *
     * @param boardTheme The board theme to find.
     * @return The name of the board theme, or {@code null} if not found.
     */
    public String getBoardThemeName(BoardTheme boardTheme) {
        return getKeyFromValue(boardThemes, boardTheme);
    }

    /**
     * Retrieves the name of a specific piece set based on its type.
     *
     * @param type     The type of the piece set.
     * @param pieceSet The piece set to find.
     * @return The name of the piece set, or {@code null} if not found.
     */
    public String getPieceSetName(PieceSetType type, PieceSet pieceSet) {
        return getKeyFromValue(pieceSets.get(type), pieceSet);
    }

    /**
     * Helper method to retrieve the key associated with a given value in a map.
     *
     * @param map   The map to search.
     * @param value The value to find.
     * @param <T>   The type of the map values.
     * @return The key associated with the value, or {@code null} if not found.
     */
    private <T> String getKeyFromValue(Map<String, T> map, T value) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
