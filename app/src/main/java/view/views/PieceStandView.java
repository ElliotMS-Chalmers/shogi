package view.views;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import util.Sfen;
import util.Side;
import view.Piece;
import view.Theme;

import java.util.*;
import java.util.function.Consumer;

public class PieceStandView extends VBox {
    private final VBox pieceStand = new VBox();
    private final Theme theme; // should probably be moved to model
    private Side side;
    private final Map<Piece, SquareView> squares = new HashMap<>() {};

    public class SquareView extends view.views.SquareView {
        private final Label label = new Label();
        private final Piece piece;
        private Integer count;

        public SquareView(Piece piece) {
            super();
            this.piece = piece;
            this.getStyleClass().add("piece-stand-square");
            this.setImage(theme.getPieceSet().getImage(piece));
            switch (side) {
                case SENTE: StackPane.setAlignment(label, Pos.BOTTOM_RIGHT); break;
                case GOTE: StackPane.setAlignment(label, Pos.TOP_RIGHT); break;
            }
            this.getChildren().add(label);
            this.prefHeightProperty().bind(
                    PieceStandView.this.heightProperty().divide(9)
            );
            this.prefWidthProperty().bind(this.heightProperty());
            setCount(0);
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
    }

    public PieceStandView(Side side, Theme theme) {
        this.side = side;
        this.theme = theme;

        this.prefHeight(Double.MAX_VALUE);
        this.getChildren().add(pieceStand);

        Piece[] pieceList = new Piece[]{};
        switch(side) {
            case SENTE:
                this.setAlignment(Pos.BOTTOM_CENTER);
                pieceList = new Piece[]{
                        Piece.SENTE_ROOK,
                        Piece.SENTE_BISHOP,
                        Piece.SENTE_GOLD_GENERAL,
                        Piece.SENTE_SILVER_GENERAL,
                        Piece.SENTE_KNIGHT,
                        Piece.SENTE_LANCE,
                        Piece.SENTE_PAWN
                };
                break;
            case GOTE:
                this.setAlignment(Pos.TOP_CENTER);
                pieceList = new Piece[] {
                        Piece.GOTE_PAWN,
                        Piece.GOTE_LANCE,
                        Piece.GOTE_KNIGHT,
                        Piece.GOTE_SILVER_GENERAL,
                        Piece.GOTE_GOLD_GENERAL,
                        Piece.GOTE_BISHOP,
                        Piece.GOTE_ROOK,
                };
                break;
        }

        for (Piece piece : pieceList) {
            SquareView square = new SquareView(piece);
            squares.put(piece, square);
            pieceStand.getChildren().add(square);
        }
    }

    public void setCapturedPieceCount(Piece piece, Integer count) {
        SquareView square = squares.get(piece);
        if (square == null) { throw new IllegalArgumentException("Invalid piece"); }
        square.setCount(count);
    }

    public void drawHand(Sfen sfen) { // THIS SHOULD PROBABLY BE MOVED TO CONTROLLER
        String capturedPieces = sfen.getCapturedPieces();
        for (int i = 0; i < capturedPieces.length()-1; i++) {
            char ch = capturedPieces.charAt(i);
            int count = 1;
            if (Character.isDigit(ch)) {
                count = Character.getNumericValue(ch);
                char nextCh = capturedPieces.charAt(++i);
                ch = nextCh;
            }
            boolean isUpperCase = Character.isUpperCase(ch);
            if ((side.equals(Side.SENTE) && isUpperCase) || (side.equals(Side.GOTE) && !isUpperCase)) {
                setCapturedPieceCount(Piece.fromSfenAbbreviation(Character.toString(ch)), count);
            }
        }
    }

    public void setClickHandler(Consumer<SquareView> clickHandler) {
        squares.forEach((piece, square) -> {
            square.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (clickHandler != null) {
                        clickHandler.accept(square);
                    }
                }
            });
        });
    }
}
