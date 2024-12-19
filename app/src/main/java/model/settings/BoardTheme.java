package model.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.InputStream;

/**
 * Represents a board theme for the Shogi game.
 * This class holds information about the board's background image and its thumbnail, as well as methods
 * to retrieve the resources associated with the theme.
 */
public class BoardTheme {

    /**
     * The name of the image file representing the board.
     */
    private final String imageName;

    /**
     * The name of the thumbnail image file representing the board in a smaller size.
     * (This is currently not used anywhere)
     */
    private final String thumbnailName;

    /**
     * The path where board images are stored.
     */
    private final String imagePath = "/image/boards/";

    /**
     * Creates a new instance of BoardTheme with the specified image and thumbnail names.
     *
     * @param image     The name of the image file representing the board.
     * @param thumbnail The name of the thumbnail image file.
     */
    @JsonCreator
    public BoardTheme(@JsonProperty("image") String image, @JsonProperty("thumbnail") String thumbnail){
        this.thumbnailName = thumbnail;
        this.imageName = image;
    }

    /**
     * Retrieves the name of the image file representing the board.
     *
     * @return The name of the image file.
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Retrieves the name of the thumbnail image file representing the board.
     *
     * @return The name of the thumbnail image file.
     */
    public String getThumbnailName() {
        return thumbnailName;
    }

    /**
     * Retrieves the background image representing the board as an InputStream.
     * The image is loaded from the resources folder, and an exception is thrown if the image is not found.
     *
     * @return The InputStream of the board image.
     * @throws IllegalArgumentException If the resource is not found.
     */
    public InputStream getBackground() {
        String path = String.format(imagePath+"%s", imageName);
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return inputStream;
    }
}

