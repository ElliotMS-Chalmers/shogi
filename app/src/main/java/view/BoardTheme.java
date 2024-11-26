package view;

import java.io.InputStream;
import javafx.scene.image.Image;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BoardTheme {
    private final String image;
    private final String thumbnail;

    @JsonCreator
    public BoardTheme(@JsonProperty("image") String image, @JsonProperty("thumbnail") String thumbnail) {
        this.image = image;
        this.thumbnail = thumbnail;
    }

//    public String getImage() {
//        return image;
//    }

    public String getThumbnail() {
        return thumbnail;
    }

     public Image getImage() {
         String path = String.format("/image/boards/%s", image);
         InputStream inputStream = getClass().getResourceAsStream(path);
         if (inputStream == null) {
             throw new IllegalArgumentException("Resource not found: " + path);
         }
         return new Image(inputStream);
     }
}
