package model;

import java.io.InputStream;
import javafx.scene.image.Image;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.pieces.Piece;

public class PieceSet {
    private final String folder;

    @JsonCreator
    public PieceSet(@JsonProperty("folder") String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }

    public Image getImage(Piece piece) {
        // implement image cache?
        String path = String.format("/image/pieces/%s/%s.png", this.folder, piece.getImageAbbreviation());
        InputStream inputStream = getClass().getResourceAsStream(path);
        Image image = new Image(inputStream);
        return image;
    }
}

