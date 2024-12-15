package view;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.function.BiConsumer;

public class MoveList extends ScrollPane {
    private final VBox content = new VBox();
    private int highlightIndex = -1;
    private BiConsumer<MoveListItem, MouseEvent> clickHandler;

    public MoveList(){
        this.getStyleClass().add("move-list");
        this.setFitToWidth(true);

        content.setAlignment(Pos.TOP_LEFT);
        content.getStyleClass().add("move-list-inner");
        this.setContent(content);
    }

    public int size(){return this.content.getChildren().size();}

    public void add(String move){
        int index = size();
        content.getChildren().add(new MoveListItem(move,index,clickHandler));
        highlight(index);
        this.setVvalue(1.0);
    }

    public void setClickHandler(BiConsumer<MoveListItem, MouseEvent> clickHandler){
        this.clickHandler = clickHandler;
    }

    public void highlight(int index){
        unhighlight();
        highlightIndex = index;
        MoveListItem mli = (MoveListItem) content.getChildren().get(index);
        mli.highlight();
    }

    public int getHighlightIndex(){return highlightIndex;}

    public void removeLastMoves(int number){
        for(int i = 0; i < number; i++){content.getChildren().removeLast();}
        if(content.getChildren().size() == 0){highlightIndex = -1;}
    }

    private void unhighlight(){
        if(highlightIndex == -1){return;}
        MoveListItem mli = (MoveListItem) content.getChildren().get(highlightIndex);
        mli.unhighlight();
    }
}