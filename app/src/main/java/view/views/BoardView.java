package view.views;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import util.Pos;
import view.Piece;
import view.Theme;

import java.util.function.Consumer;

public class BoardView extends GridPane {
    private final SquareView[][] boardSquares;
    private final Theme theme; // should probably be moved to model

    public class SquareView extends view.views.SquareView {
        public final Pos pos;

        public SquareView(Pos pos){
            super();
            this.pos = pos;
            this.getStyleClass().add("board-square");
        }

        public Pos getPos() {
            return pos;
        }
    }

    public BoardView(Integer size, Theme theme) {
        this.theme = theme;
        boardSquares = new SquareView[size][size];
        this.setId("board");
        this.prefWidthProperty().bind(this.heightProperty());

        updateBackground();
		
		for (int i = 0; i < size; i++) {
            ColumnConstraints colConstraint = new ColumnConstraints();
            colConstraint.setPercentWidth(100.0 / size);
            this.getColumnConstraints().add(colConstraint);
            
            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setPercentHeight(100.0 / size);
            this.getRowConstraints().add(rowConstraint);
        }
        
		for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                SquareView square = new SquareView(new Pos(row, col));
                boardSquares[row][col] = square;
                this.add(square, col, row);
            }
        }
    }

    public void updateBackground() {
        Image image = theme.getBoardTheme().getImage();
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

    public void drawPiece(Piece piece, Pos pos) {
        Image image = theme.getPieceSet().getImage(piece);
        SquareView square = boardSquares[pos.row()][pos.col()];
        square.setImage(image);
    }

//    public void drawPiece(Image image, Pos pos) {
//        SquareView square = boardSquares[pos.row()][pos.col()];
//        square.setImage(image);
//    }

    public void clearBoard() {
        for (SquareView[] row : boardSquares) {
            for (SquareView square : row) {
                square.setImage(null);
            }
        }
    }

    public void highlightSquare(Pos pos) {
        boardSquares[pos.row()][pos.col()].highlight();
    }

    public void clearHighlightedSquares() {
        for (SquareView[] row : boardSquares) {
            for (SquareView square : row) {
                square.unHighlight();
            }
        }
    }

    public void setClickHandler(Consumer<SquareView> clickHandler) {
        for (SquareView[] row : boardSquares) {
            for (SquareView square : row) {
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
}
