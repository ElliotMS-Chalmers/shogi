package model.settings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import util.PathResolver;

/**
 * Manages the application's user settings, such as themes, piece sets, and sound sets.
 * It provides functionality to load, save, and modify settings, as well as access resources through the {@code ResourceManager}.
 */
public class Settings {

    private final String DEFAULT_SETTINGS_FILE = "/settings.properties";
    private final Properties properties = new Properties();
    private final ResourceManager resourceManager = new ResourceManager();
    private final ObjectProperty<BoardTheme> boardTheme = new SimpleObjectProperty<>();
    private final ObjectProperty<PieceSet> standardPieceSet = new SimpleObjectProperty<>();
    private final ObjectProperty<PieceSet> chuPieceSet = new SimpleObjectProperty<>();
    private final ObjectProperty<PieceSet> kyoPieceSet = new SimpleObjectProperty<>();
    private SoundSet soundSet;

    /**
     * Constructs a new {@code Settings} instance. Loads settings from a file or the default resource,
     */
    public Settings() {
        load();
    }

    /**
     * Loads user settings from the user-specific settings file if it exists.
     * If the file does not exist, it creates a new settings file from the default resource.
     * Populates the settings properties based on the loaded data.
     */
    private void load() {
        Path userSettingsFilePath = PathResolver.getAppDataPath("/settings.properties");

        if (Files.exists(userSettingsFilePath)) {
            try (InputStream inputStream = Files.newInputStream(userSettingsFilePath)) {
                properties.load(inputStream);
            } catch (IOException e) {
                System.err.println("Failed to load settings from file: " + userSettingsFilePath + e);
            }
        } else {
            try {
                Files.createDirectories(userSettingsFilePath.getParent());
                try (InputStream inputStream = Settings.class.getResourceAsStream(DEFAULT_SETTINGS_FILE)) {
                    if (inputStream == null) {
                        throw new IllegalArgumentException("Resource not found: " + DEFAULT_SETTINGS_FILE);
                    }
                    Files.copy(inputStream, userSettingsFilePath);
                }
                try (InputStream inputStream = Files.newInputStream(userSettingsFilePath)) {
                    properties.load(inputStream);
                }
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Failed to create or load settings file: " + userSettingsFilePath + e);
            }
        }

        // currently we don't handle the case of invalid / incomplete save files
        String standardPieceSetName = properties.getProperty("standard_piece_set");
        String chuPieceSetName = properties.getProperty("chu_piece_set");
        String kyoPieceSetName = properties.getProperty("kyo_piece_set");
        String boardThemeName = properties.getProperty("board_theme");
        String soundSetName = properties.getProperty("sound_set");

        standardPieceSet.set(resourceManager.getPieceSet(PieceSetType.STANDARD, standardPieceSetName));
        chuPieceSet.set(resourceManager.getPieceSet(PieceSetType.CHU, chuPieceSetName));
        kyoPieceSet.set(resourceManager.getPieceSet(PieceSetType.KYO, kyoPieceSetName));
        boardTheme.set(resourceManager.getBoardTheme(boardThemeName));
        soundSet = resourceManager.getSoundSet(soundSetName);
    }

    /**
     * Saves the current settings to the user-specific settings file.
     * Updates the properties with the current values from the resource manager before saving.
     */
    public void save() {
        Path userSettingsFilePath = PathResolver.getAppDataPath("settings.properties");

        try {
            Files.createDirectories(userSettingsFilePath.getParent());
            try (FileOutputStream outputStream = new FileOutputStream(userSettingsFilePath.toFile())) {
                properties.setProperty("standard_piece_set", resourceManager.getPieceSetName(PieceSetType.STANDARD, standardPieceSet.get()));
                properties.setProperty("chu_piece_set", resourceManager.getPieceSetName(PieceSetType.CHU, chuPieceSet.get()));
                properties.setProperty("kyo_piece_set", resourceManager.getPieceSetName(PieceSetType.KYO, kyoPieceSet.get()));
                properties.setProperty("board_theme", resourceManager.getBoardThemeName(boardTheme.get()));

                properties.store(outputStream, "Settings");
            }
        } catch (IOException e) {
            System.err.println("Failed to save settings to file: " + userSettingsFilePath + e.getMessage());
        }
    }

    /**
     * Gets the {@link ObjectProperty} for the board theme.
     *
     * @return the {@link ObjectProperty} representing the board theme.
     */
    public ObjectProperty<BoardTheme> boardThemeProperty() {
        return boardTheme;
    }

    /**
     * Gets the {@link ObjectProperty} for the standard piece set.
     *
     * @return the {@link ObjectProperty} representing the standard piece set.
     */
    public ObjectProperty<PieceSet> standardPieceSetProperty() {
        return standardPieceSet;
    }

    /**
     * Gets the {@link ObjectProperty} for the chu piece set.
     *
     * @return the {@link ObjectProperty} representing the chu piece set.
     */
    public ObjectProperty<PieceSet> chuPieceSetProperty() {
        return chuPieceSet;
    }

    /**
     * Gets the {@link ObjectProperty} for the kyo piece set.
     *
     * @return the {@link ObjectProperty} representing the kyo piece set.
     */
    public ObjectProperty<PieceSet> kyoPieceSetProperty() {
        return kyoPieceSet;
    }

    /**
     * Gets the current standard piece set.
     *
     * @return the {@link PieceSet} for the standard game.
     */
    public PieceSet getStandardPieceSet() {
        return standardPieceSet.get();
    }

    /**
     * Gets the current chu piece set.
     *
     * @return the {@link PieceSet} for the chu game.
     */
    public PieceSet getChuPieceSet() {
        return chuPieceSet.get();
    }

    /**
     * Gets the current kyo piece set.
     *
     * @return the {@link PieceSet} for the kyo game.
     */
    public PieceSet getKyoPieceSet() {
        return kyoPieceSet.get();
    }

    /**
     * Gets the current board theme.
     *
     * @return the {@link BoardTheme}.
     */
    public BoardTheme getBoardTheme() {
        return boardTheme.get();
    }

    /**
     * Gets the current sound set.
     *
     * @return the {@link SoundSet}.
     */
    public SoundSet getSoundSet() {
        return soundSet;
    }

    /**
     * Gets all available piece sets categorized by type.
     *
     * @return a map of {@link PieceSetType} to their respective piece sets.
     */
    public Map<PieceSetType, Map<String, PieceSet>> getPieceSets() {
        return resourceManager.getPieceSets();
    }

    /**
     * Gets all available board themes.
     *
     * @return a map of theme names to their corresponding {@link BoardTheme} objects.
     */
    public Map<String, BoardTheme> getBoardThemes() {
        return resourceManager.getBoardThemes();
    }

    /**
     * Sets the board theme by name.
     *
     * @param name the name of the board theme to set.
     */
    public void setBoardTheme(String name) {
        this.boardTheme.set(resourceManager.getBoardTheme(name));
    }

    /**
     * Sets the piece set for a specific game type.
     *
     * @param type the {@link PieceSetType} (e.g., STANDARD, CHU, KYO).
     * @param name the name of the piece set to set.
     */
    public void setPieceSet(PieceSetType type, String name) {
        switch (type) {
            case STANDARD -> standardPieceSet.set(resourceManager.getPieceSet(PieceSetType.STANDARD, name));
            case CHU -> chuPieceSet.set(resourceManager.getPieceSet(PieceSetType.CHU, name));
            case KYO -> kyoPieceSet.set(resourceManager.getPieceSet(PieceSetType.KYO, name));
        }
    }

    /**
     * Gets all available sound sets.
     *
     * @return a map of sound set names to their corresponding {@link SoundSet} objects.
     */
    public Map<String, SoundSet> getSoundSets() {
        return resourceManager.getSoundSets();
    }

    /**
     * Sets the sound set by name.
     *
     * @param name the name of the sound set to set.
     */
    public void setSoundSet(String name) {
        soundSet = resourceManager.getSoundSet(name);
    }

    /**
     * Returns the name of the standard piece set.
     * This method retrieves the name of the standard piece set using the
     * `resourceManager` by passing the appropriate piece set type and
     * the currently selected standard piece set.
     *
     * @return the name of the standard piece set.
     */
    public String getStandardPieceSetName() {
        return resourceManager.getPieceSetName(PieceSetType.STANDARD, standardPieceSet.get());
    }

    /**
     * Returns the name of the currently selected Chu piece set.
     *
     * @return the name of the Chu piece set.
     */
    public String getChuPieceSetName() {
        return resourceManager.getPieceSetName(PieceSetType.CHU, chuPieceSet.get());
    }

    /**
     * Returns the name of the Kyo piece set using the
     *
     * @return the name of the Kyo piece set.
     */
    public String getKyoPieceSetName() {
        return resourceManager.getPieceSetName(PieceSetType.KYO, kyoPieceSet.get());
    }

    /**
     * Returns the name of the currently selected board theme.
     *
     * @return the name of the board theme.
     */
    public String getBoardThemeName() {
        return resourceManager.getBoardThemeName(boardTheme.get());
    }

    /**
     * Returnsthe name of the currently selected sound set.
     *
     * @return the name of the sound set.
     */
    public String getSoundSetName() {
        return resourceManager.getSoundSetName(soundSet);
    }

}
