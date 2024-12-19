package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.function.BiConsumer;

public class HistoryView extends VBox {
    private final MoveList moveList;
    private final ButtonHBox buttonHBox = new ButtonHBox();

    private class ButtonHBox extends HBox{
        private ImageView forwardImageView = new ImageView();;
        private ImageView backwardImageView = new ImageView();;
        private ImageView undoImageView = new ImageView();;
        private Button forwardButton = new Button();
        private Button backwardButton = new Button();
        private Button undoButton = new Button();

        public ButtonHBox(){
            super();
            this.getStyleClass().add("history-button-container");
            this.setAlignment(Pos.CENTER);

            forwardButton.getStyleClass().add("history-button");
            backwardButton.getStyleClass().add("history-button");
            undoButton.getStyleClass().add("history-button");

            forwardButton.prefHeightProperty().bind(this.prefHeightProperty().multiply(0.6));
            backwardButton.prefHeightProperty().bind(this.prefHeightProperty().multiply(0.6));
            undoButton.prefHeightProperty().bind(this.prefHeightProperty().multiply(0.6));
            forwardButton.prefWidthProperty().bind(forwardButton.heightProperty());
            backwardButton.prefWidthProperty().bind(backwardButton.heightProperty());
            undoButton.prefWidthProperty().bind(undoButton.heightProperty());

            ImageView[] images = {forwardImageView,backwardImageView,undoImageView};
            for(ImageView image : images){
                image.fitHeightProperty().bind(forwardButton.prefHeightProperty().multiply(0.6));
                image.setPreserveRatio(true);
            }
            forwardButton.setGraphic(forwardImageView);
            backwardButton.setGraphic(backwardImageView);
            undoButton.setGraphic(undoImageView);

            this.getChildren().addAll(
                    createRegion(),backwardButton,createRegion(),
                    undoButton,createRegion(),forwardButton,createRegion()
            );
            setImages();
        }

        public void setClickHandlers(BiConsumer<Button, ActionEvent> forwardHandler,
                                     BiConsumer<Button, ActionEvent> backwardHandler,
                                     BiConsumer<Button, ActionEvent> undoHandler){
            setClickHandler(forwardButton,forwardHandler);
            setClickHandler(backwardButton,backwardHandler);
            setClickHandler(undoButton,undoHandler);
        }

        private void setClickHandler(Button button, BiConsumer<Button,ActionEvent> handler){
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (handler != null) {
                        handler.accept(button, event);
                    }
                }
            });
        }

        private Region createRegion(){
            //För att avståndet mellan knapparna ska uppdateras beroende av storleken på ButtonHBox
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            return region;
        }

        private void setImages(){
            forwardImageView.setImage(new Image("image/button_icons/forward.png"));
            backwardImageView.setImage(new Image("image/button_icons/backward.png"));
            undoImageView.setImage(new Image("image/button_icons/undo.png"));
        }
    }

    public HistoryView(){
        super();
        this.setId("History");
        this.getStyleClass().add("history-view");
        this.setPrefHeight(this.prefHeight(Double.MAX_VALUE));
        this.prefWidthProperty().bind(this.heightProperty().multiply(0.35));
        this.setMinWidth(100);
        moveList = new MoveList();
        moveList.prefHeightProperty().bind(this.heightProperty().multiply(0.9));
        buttonHBox.prefHeightProperty().bind(this.heightProperty().multiply(0.1));
        this.getChildren().addAll(moveList,buttonHBox);
    }

    public void addMove(String move){
        moveList.add(move);
    }

    public void highlight(int index){
        moveList.highlight(index);
    }

    public int getHighlightIndex(){return moveList.getHighlightIndex();}

    public void removeLastMoves(int number){moveList.removeLastMoves(number);}

    public void setMoveClickHandler(BiConsumer<MoveListItem, MouseEvent> clickHandler){
        moveList.setClickHandler(clickHandler);
    }

    public void setButtonClickHandler(BiConsumer<Button, ActionEvent> forwardHandler,
                                      BiConsumer<Button, ActionEvent> backwardHandler,
                                      BiConsumer<Button, ActionEvent> undoHandler){
        buttonHBox.setClickHandlers(forwardHandler,backwardHandler,undoHandler);
    }
}