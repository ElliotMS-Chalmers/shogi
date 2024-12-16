package model.settings;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.pieces.Piece;

public class PieceSet {
    private final String directory;
    private final Map<String, byte[]> imageCache = new HashMap<>() {};

    @JsonCreator
    public PieceSet(@JsonProperty("directory") String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public InputStream getImage(Piece piece) {
        if (piece == null) { return null; } // temporary while CHU and KYO pieces dont exist
//        String abbr = piece.getImageAbbreviation();
        String abbr = piece.getSfenAbbreviation();
        if (imageCache.containsKey(abbr)) {
            return new ByteArrayInputStream(imageCache.get(abbr));
        }

        String path = String.format("/image/pieces/%s/%s/%s.png", directory, piece.getSide().toString().toLowerCase(), abbr);
        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            if (inputStream != null) {
                byte[] imageData = inputStream.readAllBytes();
                imageCache.put(abbr, imageData);
                return new ByteArrayInputStream(imageData); // new inputstream because old one was consumed I think
            } else {
                throw new RuntimeException("Image not found: " + path);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading image: " + path, e);
        }
    }
}

