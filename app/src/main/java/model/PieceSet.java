package model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.pieces.Piece;

public class PieceSet {
    private final String folder;
    private final Map<String, byte[]> imageCache = new HashMap<>() {};

    @JsonCreator
    public PieceSet(@JsonProperty("folder") String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }

    public Image getImage(Piece piece) {
        String abbr = piece.getImageAbbreviation();
        if (imageCache.containsKey(abbr)) {
            return new Image(new ByteArrayInputStream(imageCache.get(abbr)));
        }

        String path = String.format("/image/pieces/%s/%s.png", folder, abbr);
        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            if (inputStream != null) {
                byte[] imageData = inputStream.readAllBytes();
                imageCache.put(abbr, imageData);
                return new Image(new ByteArrayInputStream(imageData)); // TODO: make not dependent on javafx Image class (MVC)
            } else {
                throw new RuntimeException("Image not found: " + path);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading image: " + path, e);
        }
    }
}

