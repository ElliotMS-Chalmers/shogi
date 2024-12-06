package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class SquareView extends StackPane {
    protected ImageView imageView;

    public SquareView(){
        imageView = new ImageView();
        imageView.setManaged(false); // important!
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        this.getChildren().add(imageView);
        this.getStyleClass().add("square");
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }

    public void highlight() {
        this.getStyleClass().add("selected");
    }

    public void unHighlight() {
        this.getStyleClass().remove("selected");
    }

    public void mark() {
        this.getStyleClass().add("marked");
    }

    public void unMark() {
        this.getStyleClass().remove("marked");
    }
}
