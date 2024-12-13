package view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.InputStream;
import java.util.function.BiConsumer;

public class HistoryView extends VBox {
    private final MoveList moveList;

    public HistoryView(){
        super();
        this.setId("History");
        // this.setBackground(backgroundImagePath);
        this.getStyleClass().add("history-view");
        this.setPrefHeight(this.prefHeight(Double.MAX_VALUE));
        this.prefWidthProperty().bind(this.heightProperty().multiply(0.35));
        moveList = new MoveList();
        moveList.prefHeightProperty().bind(this.heightProperty().multiply(0.9));
        this.getChildren().add(moveList);
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
