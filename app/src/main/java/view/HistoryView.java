package view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.InputStream;
import java.util.function.BiConsumer;

public class HistoryView extends VBox {
    private final String backgroundImagePath = "/image/historyBackground.png"; //Kanske inte ska hårdkodas här
    private final MoveList moveList;

    public HistoryView(){
        super();
        this.setId("History");
        this.setBackground(backgroundImagePath);
        this.getStyleClass().add("history-view");
        this.setPrefHeight(this.prefHeight(Double.MAX_VALUE));
        this.prefWidthProperty().bind(this.heightProperty().multiply(0.5));

        moveList = new MoveList();
        moveList.prefHeightProperty().bind(this.heightProperty().multiply(0.8));
        this.getChildren().add(moveList);
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
        moveList.add(move);
    }
    public void highlight(int index){
        moveList.highlight(index);
    }
    public int getHighlightIndex(){return moveList.getHighlightIndex();}
    public void setMoveClickHandler(BiConsumer<MoveListItem, MouseEvent> clickHandler){
        moveList.setClickHandler(clickHandler);
    }
    public void setButtonClickHandler(){}

}
