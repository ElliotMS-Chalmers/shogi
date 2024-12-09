package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.image.Image;

import java.io.InputStream;

public class BoardTheme{
    private final String imageName;
    private final String thumbnailName;
    private final String imagePath = "/image/boards/";
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

    public Image getImage() {
        String path = String.format(imagePath+"%s", imageName);
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return new Image(inputStream);
    }
}
