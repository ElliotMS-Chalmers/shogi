package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import util.Piece;
import util.Sfen;

public class BoardView {
    private final GridPane gridPane = new GridPane();
    private final BoardSquare[][] boardSquares;
    private final Theme theme;

    private class BoardSquare {
        private final StackPane stackPane;
        private ImageView imageView;

        public BoardSquare() {
            stackPane = new StackPane();
            imageView = new ImageView();
            imageView.setManaged(false); // important!
            imageView.setPreserveRatio(true);
            imageView.fitWidthProperty().bind(stackPane.widthProperty());
            imageView.fitHeightProperty().bind(stackPane.heightProperty());
            stackPane.getChildren().add(imageView);

            stackPane.setStyle("-fx-border-color: black; -fx-border-width: 0.5; -fx-padding: 0;");
        }

        public StackPane getView() {
            return stackPane;
        }

        public void setImage(Image image) {
            imageView.setImage(image);
        }
    }

    public BoardView(Integer size, Theme theme) {
        this.theme = theme;
        boardSquares = new BoardSquare[size][size];

        gridPane.setStyle("-fx-border-width: 0.5; -fx-border-color: black; -fx-padding: 0;");
        gridPane.prefWidthProperty().bind(gridPane.heightProperty());

        updateBackground();
		
		for (int i = 0; i < size; i++) {
            ColumnConstraints colConstraint = new ColumnConstraints();
            colConstraint.setPercentWidth(100.0 / size);
            gridPane.getColumnConstraints().add(colConstraint);
            
            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setPercentHeight(100.0 / size);
            gridPane.getRowConstraints().add(rowConstraint);
        }
        
		for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                BoardSquare square = new BoardSquare();
                boardSquares[row][col] = square;
                gridPane.add(square.getView(), col, row);
            }
        }
    }

    public GridPane getView() {
		return gridPane;
	}

    public void updateBackground() {
        Image image = theme.boardTheme.getImage(); 
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );

        Background background = new Background(backgroundImage);
        gridPane.setBackground(background);
    }

    public void drawPiece(Piece piece, Integer row, Integer col) {
        Image image = theme.getPieceSet().getImage(piece);
        BoardSquare square = boardSquares[row][col];
        square.setImage(image);
    }

    public void drawBoard(Sfen sfen) {
        String[] rows = sfen.getRows();
        for (int row = 0; row < rows.length; row++) {
            int col = 0;
            for (char ch : rows[row].toCharArray()) {
                if (Character.isDigit(ch)) {
                    col += Character.getNumericValue(ch);
                } else if (ch == '+') {
                    char nextCh = rows[row].charAt(col+1);
                    drawPiece(Piece.fromSfenAbbreviation("+" + nextCh), row, col);
                    col += 2;
                } else {
                    drawPiece(Piece.fromSfenAbbreviation(Character.toString(ch)), row, col);
                    col += 1;
                }
            }
        }
    }

    public void clearBoard() {
        for (BoardSquare[] row : boardSquares) {
            for (BoardSquare square : row) {
                square.setImage(null);
            }
        }
    }
}
