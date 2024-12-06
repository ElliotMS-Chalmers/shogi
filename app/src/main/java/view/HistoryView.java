package view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.InputStream;
import java.util.List;
import java.util.Stack;

public class HistoryView extends VBox {
    private Stack<String> moveList = new Stack<>();

    private final String backgroundImagePath = "/image/historyBackground.png"; //Kanske inte ska hårdkodas här

    public HistoryView(){
        super();
        this.setId("History");
        this.setAlignment(Pos.CENTER_RIGHT);
        this.setBackground(backgroundImagePath);
        VBox.setVgrow(this,Priority.ALWAYS);

    }

    public void setBackground(String imageName) {
        Image image = getBackgroundImage();
        //Kopierat från BoardView
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );

        Background background = new Background(backgroundImage);
        this.setBackground(background);
    }
    public Image getBackgroundImage() {
        String path = String.format(backgroundImagePath);
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return new Image(inputStream);
    }

    public void addMove(String move){
        moveList.push(move);
    }
    public String removeLastMove(){
        return moveList.pop();
    }
    public void setMoveList(List<String> moves){
        moveList.clear();
        moveList.addAll(moves);
    }

}
