package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import util.Side;

public class ShogiView extends HBox {
    private final BoardView boardView;
    private final PieceStandView gotePieceStandView;
    private final PieceStandView sentePieceStandView;
    private final HistoryView historyView;

    public ShogiView(int boardSize, int handSize) {
        this.boardView = new BoardView(boardSize);
        this.gotePieceStandView = new PieceStandView(Side.GOTE, handSize, boardSize);
        this.sentePieceStandView = new PieceStandView(Side.SENTE, handSize, boardSize);
        this.historyView = new HistoryView();
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(0, 25, 0, 25));

        HBox centerGroup = new HBox(10);
        centerGroup.setAlignment(Pos.CENTER);
        centerGroup.getChildren().addAll(gotePieceStandView, boardView, sentePieceStandView);
        // centerGroup.setStyle("-fx-background-color: blue;");

        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.NEVER);
        // leftSpacer.setStyle("-fx-background-color: red;");
        leftSpacer.prefWidthProperty().bind(historyView.widthProperty());

        boardView.minWidthProperty().bind(boardView.heightProperty());
        gotePieceStandView.minWidthProperty().bind(boardView.widthProperty().divide(boardSize));
        sentePieceStandView.minWidthProperty().bind(boardView.widthProperty().divide(boardSize));


        this.getChildren().addAll(leftSpacer, centerGroup, historyView);
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public PieceStandView getGotePieceStandView() {
        return gotePieceStandView;
    }

    public PieceStandView getSentePieceStandView() {
        return sentePieceStandView;
    }

    public HistoryView getHistoryView(){return historyView;}
}
