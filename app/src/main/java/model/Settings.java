package model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {
    Properties properties = new Properties();
    ThemeManager themeManager = new ThemeManager();
    private PieceSet pieceSet;
    private BoardTheme boardTheme;

    public Settings(String path) {
        try (InputStream inputStream = Settings.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }
            properties.load(inputStream);
            String pieceSetName = properties.getProperty("piece_set");
            String boardThemeName = properties.getProperty("board_theme");
            pieceSet = themeManager.getPieceSet(pieceSetName);
            boardTheme = themeManager.getBoardTheme(boardThemeName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PieceSet getPieceSet() {
        return pieceSet;
    }

    public BoardTheme getBoardTheme() {
        return boardTheme;
    }
}
