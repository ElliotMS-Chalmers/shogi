package view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import util.Pos;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BoardView extends GridPane {
    private final SquareView[][] squares;
    private ClockView senteClockView, goteClockView;

    public class SquareView extends view.SquareView {
        public final Pos pos;

        public SquareView(Pos pos){
            super();
            this.pos = pos;
            this.getStyleClass().add("board-square");
        }

        public Pos getPos() {
            return pos;
        }

        public void setPromotable() {
            this.getStyleClass().add("promotable");
        }

        public void removePromotable() {
            this.getStyleClass().remove("promotable");
        }

        public boolean isPromotable() { return this.getStyleClass().contains("promotable"); }
    }

    public BoardView(Integer size) {
        squares = new SquareView[size][size];
        this.setId("board");
        this.minWidthProperty().bind(this.heightProperty());

        // fixes resizing bug when pressing "maximize" button
        this.minWidthProperty().addListener((observable, oldValue, newValue) -> {
            this.setWidth(newValue.doubleValue());
        });

		setContraints();
		populateGrid();
    }

    private void setContraints() {
        int size = squares.length;
        for (int i = 0; i < size; i++) {
            ColumnConstraints colConstraint = new ColumnConstraints();
            colConstraint.setPercentWidth(100.0 / size);
            this.getColumnConstraints().add(colConstraint);

            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setPercentHeight(100.0 / size);
            this.getRowConstraints().add(rowConstraint);
        }
    }

    private void populateGrid() {
        int size = squares.length;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                SquareView square = new SquareView(new Pos(row, col));
                squares[row][col] = square;
                this.add(square, col, row);
            }
        }
    }

    public void setBackground(Image image) {
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

    public void drawImageAt(Image image, Pos pos) {
        SquareView square = squares[pos.row()][pos.col()];
        square.setImage(image);
    }

    public void clearPieces() {
        for (SquareView[] row : squares) {
            for (SquareView square : row) {
                square.setImage(null);
            }
        }
    }

    public void highlightSquare(Pos pos) {
        squares[pos.row()][pos.col()].highlight();
    }

    public void clearHighlightedSquares() {
        forEachSquare(view.SquareView::unHighlight);
    }

    public void setClickHandler(BiConsumer<SquareView, MouseEvent> clickHandler) {
        for (SquareView[] row : squares) {
            for (SquareView square : row) {
                square.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (clickHandler != null) {
                            clickHandler.accept(square, event);
                        }
                    }
                });
            }
        }
    }

    public void markSquare(Pos pos) {
        if (pos.row() > squares.length - 1 || pos.col() > squares[0].length - 1) { return; }
        squares[pos.row()][pos.col()].mark();
    }

    public void clearMarkedSquares() {
        forEachSquare(view.SquareView::unMark);
    }

    public void setPromotableSquare(Pos pos) {
        squares[pos.row()][pos.col()].setPromotable();
    }

    public void clearPromotableSquares() {
        forEachSquare(BoardView.SquareView::removePromotable);
    }

    private void forEachSquare(Consumer<SquareView> handler) {
        for (SquareView[] row : squares) {
            for (SquareView square : row) {
                handler.accept(square);
            }
        }
    }
}
