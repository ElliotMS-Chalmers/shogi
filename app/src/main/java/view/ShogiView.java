package view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import util.Side;

public class ShogiView extends VBox {
    private final BoardView boardView;
    private final PieceStandView gotePieceStandView;
    private final PieceStandView sentePieceStandView;
    private final HistoryView historyView;
    private final GameMenu gameMenuView;
    private final ClockView senteClockView, goteClockView;

    public ShogiView(int boardSize, int handSize) {
        this.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        VBox.setVgrow(hBox, Priority.ALWAYS);
        this.getChildren().add(hBox);
        hBox.maxHeightProperty().bind(this.heightProperty().multiply(0.8));
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(15);
        hBox.setPadding(new Insets(0, 25, 0, 25));

        this.boardView = new BoardView(boardSize);
        this.gotePieceStandView = new PieceStandView(Side.GOTE, handSize, boardSize);
        this.sentePieceStandView = new PieceStandView(Side.SENTE, handSize, boardSize);
        this.historyView = new HistoryView();
        this.gameMenuView = new GameMenu();
        this.senteClockView = new ClockView(Side.SENTE);
        this.goteClockView = new ClockView(Side.GOTE);


        // HBox centerGroup = new HBox(10);
        // centerGroup.setAlignment(Pos.CENTER);
        // centerGroup.getChildren().addAll(gotePieceStandView, boardView, sentePieceStandView);

        // Region leftSpacer = new Region();
        // HBox.setHgrow(leftSpacer, Priority.NEVER);
        // leftSpacer.setStyle("-fx-background-color: red;");
        // leftSpacer.prefWidthProperty().bind(historyView.widthProperty());

        gotePieceStandView.minWidthProperty().bind(boardView.widthProperty().divide(boardSize));
        sentePieceStandView.minWidthProperty().bind(boardView.widthProperty().divide(boardSize));
        hBox.getChildren().addAll(senteClockView, goteClockView, gotePieceStandView, boardView, sentePieceStandView, historyView);
    }


    public ClockView getGoteClockView() {
        return goteClockView;
    }

    public ClockView getSenteClockView() {
        return senteClockView;
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
