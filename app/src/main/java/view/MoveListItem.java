package view;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.function.BiConsumer;

public class MoveListItem extends Pane {
    private final String move;
    private final int index;

    public MoveListItem(String move, int index, BiConsumer<MoveListItem, MouseEvent> clickHandler){
        this.move = move;
        this.index = index;
        this.setPrefWidth(Double.MAX_VALUE);

        Label label = new Label((index+1)+": "+move);
        label.getStyleClass().add("move-list-item-text");
        this.getChildren().add(label);

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