package model;

import java.io.InputStream;
import javafx.scene.image.Image;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BoardTheme {
    private final String imageName;
    private final String thumbnailName;

    @JsonCreator
    public BoardTheme(@JsonProperty("image") String image, @JsonProperty("thumbnail") String thumbnail) {
        this.imageName = image;
        this.thumbnailName = thumbnail;
    }
    public String getImageName() {
        return imageName;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public Image getImage() {
        String path = String.format("/image/boards/%s", imageName);
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return new Image(inputStream);
    }
}
