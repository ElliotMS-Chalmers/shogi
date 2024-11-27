package view.views;

import controller.GameController;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import util.Pos;
import util.Sfen;
import view.Piece;
import view.Theme;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BoardView extends GridPane {
    private final BoardSquare[][] boardSquares;
    private final Theme theme;
    private Consumer<Pos> clickHandler;

    private class BoardSquare extends StackPane {
        private ImageView imageView;
        private Pos pos;

        public BoardSquare(Pos pos){
            this.pos = pos;
            imageView = new ImageView();
            imageView.setManaged(false); // important!
            imageView.setPreserveRatio(true);
            imageView.fitWidthProperty().bind(this.widthProperty());
            imageView.fitHeightProperty().bind(this.heightProperty());
            this.getChildren().add(imageView);

            this.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (clickHandler != null) {
                        clickHandler.accept(pos);
                    }
                }
            });
            this.getStyleClass().add("board-square");
        }

        public void setImage(Image image) {
            imageView.setImage(image);
        }

        public void highlight() {
            this.getStyleClass().add("selected");
        }

        public void unHighlight() {
            this.getStyleClass().remove("selected");
        }
    }

    public BoardView(Integer size, Theme theme) {
        this.theme = theme;
        boardSquares = new BoardSquare[size][size];
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
                BoardSquare square = new BoardSquare(new Pos(row, col));
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
        BoardSquare square = boardSquares[pos.row()][pos.col()];
        square.setImage(image);
    }

    public void drawBoard(Sfen sfen) {
        sfen.forEachPiece((abbr, pos) -> {
            drawPiece(Piece.fromSfenAbbreviation(abbr), pos);
        });
    }

    public void clearBoard() {
        for (BoardSquare[] row : boardSquares) {
            for (BoardSquare square : row) {
                square.setImage(null);
            }
        }
    }

    public void highlightSquare(Pos pos) {
        boardSquares[pos.row()][pos.col()].highlight();
    }

    public void clearHighlightedSquares() {
        for (BoardSquare[] row : boardSquares) {
            for (BoardSquare square : row) {
                square.unHighlight();
            }
        }
    }

    public void setClickHandler(Consumer<Pos> clickHandler) {
        this.clickHandler = clickHandler;
    }
}
