package view;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

import java.util.function.BiConsumer;

public class MoveListItem extends HBox {
    private final String move;
    private final int index;

    public MoveListItem(String move, int index, BiConsumer<MoveListItem, MouseEvent> clickHandler){
        this.move = move;
        this.index = index;
        this.getStyleClass().add("move-list-item");

        StackPane indexPane = new StackPane(new Label(String.valueOf(index + 1)));
        indexPane.setMinWidth(15);
        indexPane.prefWidthProperty().bind(this.widthProperty().multiply(0.125));
        indexPane.getStyleClass().add("move-list-item-index");

        Pane movePane = new Pane(new Label(move));
        movePane.getStyleClass().add("move-list-item-move");
        HBox.setHgrow(movePane, Priority.ALWAYS);

        this.getChildren().addAll(indexPane, movePane);

        setClickHandler(clickHandler);
    }
    public int getIndex(){return index;}

    public void highlight(){
        this.getStyleClass().add("move-list-item-highlighted");
    }

    public void unhighlight(){
        this.getStyleClass().remove("move-list-item-highlighted");
    }

    private void setClickHandler(BiConsumer<MoveListItem,MouseEvent> clickHandler){
        this.setOnMousePressed(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (clickHandler != null) {
                    clickHandler.accept(MoveListItem.this, event);
                }
            }
        });
    }
}