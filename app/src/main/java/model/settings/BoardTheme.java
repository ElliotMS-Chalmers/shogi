package model.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.image.Image;

import java.io.InputStream;

public class BoardTheme {
    private final String imageName;
    private final String thumbnailName;
    private final String directory = "/image/boards/";

    @JsonCreator
    public BoardTheme(@JsonProperty("image") String image, @JsonProperty("thumbnail") String thumbnail){
        this.thumbnailName = thumbnail;
        this.imageName = image;
    }
    public String getImageName() {
        return imageName;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public InputStream getImage() {
        String path = String.format(directory+"%s", imageName);
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return inputStream;
    }
}
