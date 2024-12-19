package model.settings;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.pieces.Piece;

/**
 * Represents a set of piece images for a Shogi game.
 * This class manages the directory containing the piece images and provides functionality
 * to retrieve images for specific pieces. Images are cached in memory for performance optimization.
 */
public class PieceSet {

    /**
     * The directory containing the images for the pieces.
     */
    private final String directory;

    /**
     * A cache to store loaded piece images in memory, keyed by the piece's abbreviation.
     */
    private final Map<String, byte[]> imageCache = new HashMap<>();

    /**
     * Creates a new instance of {@code PieceSet} with the specified directory name.
     *
     * @param directory The directory containing the piece images.
     */
    @JsonCreator
    public PieceSet(@JsonProperty("directory") String directory) {
        this.directory = directory;
    }

    /**
     * Retrieves the directory name containing the piece images.
     *
     * @return The directory name.
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Retrieves the image for the specified piece as an {@link InputStream}.
     * <p>
     * The image is loaded from the directory specified in this {@code PieceSet} and cached in memory.
     * If the image is already in the cache, it is returned directly. Otherwise, the image is read
     * from the resources folder, cached, and then returned.
     *
     * @param piece The {@link Piece} for which the image is to be retrieved.
     * @return An {@link InputStream} containing the image data, or {@code null} if the piece is {@code null}.
     * @throws RuntimeException If the image cannot be found or an error occurs during loading.
     */
    public InputStream getImage(Piece piece) {
        if (piece == null) {
            return null; // Temporary handling for unsupported pieces like CHU and KYO.
        }

        String abbr = piece.getSfenAbbreviation();
        if (imageCache.containsKey(abbr)) {
            return new ByteArrayInputStream(imageCache.get(abbr));
        }

        String path = String.format("/image/pieces/%s/%s/%s.png", directory, piece.getSide().toString().toLowerCase(), abbr);
        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            if (inputStream != null) {
                byte[] imageData = inputStream.readAllBytes();
                imageCache.put(abbr, imageData);
                return new ByteArrayInputStream(imageData); // Return a new InputStream for the cached image.
            } else {
                throw new RuntimeException("Image not found: " + path);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading image: " + path, e);
        }
    }
}
