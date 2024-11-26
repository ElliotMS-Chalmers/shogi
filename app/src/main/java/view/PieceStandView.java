package view;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import util.Piece;
import util.Sfen;
import util.Side;

import java.util.*;

public class PieceStandView {
    private final VBox wrapper = new VBox();
    private final VBox pieceStand = new VBox();
    private final Theme theme;
    private Side side;
    private final Map<Piece, PieceStandSquare> squares = new HashMap<>() {};

    private class PieceStandSquare {
        private final StackPane stackPane = new StackPane();
        private final ImageView imageView;
        private Label label = new Label();
        private Integer count;

        public PieceStandSquare(Piece piece) {
            imageView = new ImageView(theme.getPieceSet().getImage(piece));
            imageView.setManaged(false); // important!
            imageView.setPreserveRatio(true);
            imageView.fitWidthProperty().bind(stackPane.widthProperty());
            imageView.fitHeightProperty().bind(stackPane.heightProperty());
            stackPane.getChildren().add(imageView);

            label.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-border-width: 1; -fx-border-radius: 3; -fx-border-color: #474747; -fx-background-color: white; -fx-background-radius: 3; -fx-padding: -3 3 -3 3;");
            switch (side) {
                case SENTE: StackPane.setAlignment(label, Pos.BOTTOM_RIGHT); break;
                case GOTE: StackPane.setAlignment(label, Pos.TOP_RIGHT); break;
            }
            stackPane.setStyle("-fx-padding: 0 3 0 0");
            stackPane.getChildren().add(label);

            stackPane.prefHeightProperty().bind(
                    wrapper.heightProperty().divide(9)
            );
            stackPane.prefWidthProperty().bind(stackPane.heightProperty());
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

        public StackPane getView() {
            return stackPane;
        }
    }

    public PieceStandView(Side side, Theme theme) {
        this.side = side;
        this.theme = theme;

        wrapper.prefHeight(Double.MAX_VALUE);
        wrapper.getChildren().add(pieceStand);

        Piece[] pieceList = new Piece[]{};
        switch(side) {
            case SENTE:
                wrapper.setAlignment(Pos.BOTTOM_CENTER);
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
                wrapper.setAlignment(Pos.TOP_CENTER);
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
            PieceStandSquare square = new PieceStandSquare(piece);
            squares.put(piece, square);
            pieceStand.getChildren().add(square.getView());
        }
    }

    public void setCapturedPieceCount(Piece piece, Integer count) {
        PieceStandSquare square = squares.get(piece);
        if (square == null) { throw new IllegalArgumentException("Invalid piece"); }
        square.setCount(count);
    }

    public VBox getView() {
        return wrapper;
    }

    public void drawHand(Sfen sfen) {
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
}
