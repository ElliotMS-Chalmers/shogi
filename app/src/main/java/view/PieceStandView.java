package view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import util.Side;

import java.util.function.Consumer;

public class PieceStandView extends VBox {
    private final Side side;
    private final SquareView[] squares;
    private final VBox squareWrapper = new VBox(); // Needed to we can align squares to top or bottom of piece stand depending on side
    private final int scaleFactor;

    public class SquareView extends view.SquareView {
        private final int index;
        private final Side side;
        private final Label label = new Label();
        private Integer count;

        public SquareView(Side side, int index) {
            super();
            this.side = side;
            this.index = index;
            this.getStyleClass().add("piece-stand-square");
            createLabel();
            this.prefHeightProperty().bind(
                PieceStandView.this.heightProperty().divide(scaleFactor)
            );
            this.prefWidthProperty().bind(this.heightProperty());
            setCount(0);
        }

        private void createLabel() {
            switch (side) {
                case SENTE: StackPane.setAlignment(label, Pos.BOTTOM_RIGHT); break;
                case GOTE: StackPane.setAlignment(label, Pos.TOP_RIGHT); break;
            }
            this.getChildren().add(label);
        }

        public void setCount(Integer count) {
            this.count = count;
            if (count == 0) {
                imageView.setOpacity(0.50);
                label.setVisible(false);
            } else {
                imageView.setOpacity(1);
                label.setVisible(true);
            }
            label.setText(count.toString());
        }

        public int getIndex() {
            return index;
        }

        public Side getSide() {
            return side;
        }

        public Integer getCount() {
            return count;
        }
    }

    public PieceStandView(Side side, int size, int scaleFactor) {
        this.side = side;
        this.squares = new SquareView[size];
        this.scaleFactor = scaleFactor;
        this.getChildren().add(squareWrapper);
        this.prefHeight(Double.MAX_VALUE);

        switch(side) {
            case SENTE: this.setAlignment(Pos.BOTTOM_CENTER); break;
            case GOTE: this.setAlignment(Pos.TOP_CENTER); break;
        }

        populateGrid();
    }

    private void populateGrid() {
        for (int i = 0; i < squares.length; i++) {
            SquareView square = new SquareView(side, i);
            squares[i] = square;
            squareWrapper.getChildren().add(square);
        }
    }

    public void setCountAt(Integer count, int index) {
        SquareView square = squares[index];
        square.setCount(count);
    }

    public void changeCountAt(int index, int change){
        SquareView square = squares[index];
        square.setCount(square.getCount()+change);
    }

    public void drawImageAt(Image image, int index) {
        SquareView square = squares[index];
        square.setImage(image);
    }

    public void unHighlightSquares(){
        for(PieceStandView.SquareView square : squares){
            square.unHighlight();
        }
    }

    public int getSize() {
        return squares.length;
    }

    public void setClickHandler(Consumer<SquareView> clickHandler) {
        for (int i = 0; i < squares.length; i++) {
            SquareView square = squares[i];
            square.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (clickHandler != null) {
                        clickHandler.accept(square);
                    }
                }
            });
        }
    }
}
