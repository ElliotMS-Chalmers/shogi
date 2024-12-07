package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Settings {
    private final String DEFAULT_SETTINGS_FILE = "/settings.properties";
    private final Properties properties = new Properties();
    private final ThemeManager themeManager = new ThemeManager();
    private final ObjectProperty<BoardTheme> boardTheme = new SimpleObjectProperty<>();
    private final ObjectProperty<PieceSet> pieceSet = new SimpleObjectProperty<>();

    public Settings() {
        loadSettings();
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveSettings));
    }

    private void loadSettings() {
        Path userSettingsFilePath = getUserSettingsFilePath();

        if (Files.exists(userSettingsFilePath)) {
            // If the file exists, load from the user settings file
            try (InputStream inputStream = Files.newInputStream(userSettingsFilePath)) {
                properties.load(inputStream);
            } catch (IOException e) {
                logError("Failed to load settings from file: " + userSettingsFilePath, e);
            }
        } else {
            // If the file doesn't exist, create the file and copy from the default resource
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
                logError("Failed to create or load settings file: " + userSettingsFilePath, e);
            }
        }

        // Set attributes
        String pieceSetName = properties.getProperty("piece_set");
        String boardThemeName = properties.getProperty("board_theme");
        pieceSet.set(themeManager.getPieceSet(pieceSetName));
        boardTheme.set(themeManager.getBoardTheme(boardThemeName));
    }

    private void saveSettings() {
        Path userSettingsFilePath = getUserSettingsFilePath();

        try {
            Files.createDirectories(userSettingsFilePath.getParent());
            try (FileOutputStream outputStream = new FileOutputStream(userSettingsFilePath.toFile())) {
                properties.setProperty("piece_set", themeManager.getPieceSetName(pieceSet.get()));
                properties.setProperty("board_theme", themeManager.getBoardThemeName(boardTheme.get()));

                properties.store(outputStream, "Settings");
            }
        } catch (IOException e) {
            logError("Failed to save settings to file: " + userSettingsFilePath, e);
        }
    }

    private Path getUserSettingsFilePath() {
        String userHome = System.getProperty("user.home");
        String os = System.getProperty("os.name").toLowerCase();

        Path settingsFilePath;

        if (os.contains("win")) {
            settingsFilePath = Paths.get(userHome, "AppData", "Local", "Shogi", "settings.properties");
        } else {
            settingsFilePath = Paths.get(userHome, ".config", "Shogi", "settings.properties");
        }

        return settingsFilePath;
    }

    private void logError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }

    public ObjectProperty<BoardTheme> boardThemeProperty() {
        return boardTheme;
    }

    public ObjectProperty<PieceSet> pieceSetProperty() {
        return pieceSet;
    }

    public PieceSet getPieceSet() {
        return pieceSet.get();
    }

    public BoardTheme getBoardTheme() {
        return boardTheme.get();
    }

    public Map<String, PieceSet> getPieceSets() {
        return themeManager.getPieceSets();
    }

    public Map<String, BoardTheme> getBoardThemes() {
        return themeManager.getBoardThemes();
    }

    public void setBoardTheme(String name) {
        this.boardTheme.set(themeManager.getBoardTheme(name));
    }

    public void setPieceSet(String name) {
        this.pieceSet.set(themeManager.getPieceSet(name));
    }
}
