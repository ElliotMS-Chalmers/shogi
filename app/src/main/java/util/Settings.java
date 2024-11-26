package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {
    Properties properties = new Properties();
    private String pieceSet;
    private String boardTheme;

    public Settings(String path) {
        try (InputStream inputStream = Settings.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }
            properties.load(inputStream);
            pieceSet = properties.getProperty("piece_set");
            boardTheme = properties.getProperty("board_theme");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPieceSet() {
        return pieceSet;
    }

    public String getBoardTheme() {
        return boardTheme;
    }
}
