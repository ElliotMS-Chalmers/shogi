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
        // Set alignment of the parent container
        this.setAlignment(Pos.CENTER);

        // Create the main container (HBox) and configure it
        HBox hBox = new HBox();
        VBox.setVgrow(hBox, Priority.ALWAYS);
        this.getChildren().add(hBox);
        hBox.maxHeightProperty().bind(this.heightProperty().multiply(0.85));
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(15);
        hBox.setPadding(new Insets(0, 25, 0, 25));

        // Create the views for different parts of the UI
        this.boardView = new BoardView(boardSize);
        this.gotePieceStandView = new PieceStandView(Side.GOTE, handSize, boardSize);
        this.sentePieceStandView = new PieceStandView(Side.SENTE, handSize, boardSize);
        this.historyView = new HistoryView();
        this.gameMenuView = new GameMenu();
        this.senteClockView = new ClockView(Side.SENTE);
        this.goteClockView = new ClockView(Side.GOTE);

        // Setup board and piece stands inside an HBox
        HBox boardWrapper = new HBox(10);
        boardWrapper.getChildren().addAll(gotePieceStandView, boardView, sentePieceStandView);

        // Create a wrapper for clocks and the board
        VBox clockAndBoardWrapper = new VBox(5);
        clockAndBoardWrapper.setAlignment(Pos.CENTER);
        VBox.setVgrow(boardWrapper, Priority.ALWAYS);

        // Create and configure the clock wrappers
        HBox goteClockWrapper = new HBox(goteClockView);
        HBox senteClockWrapper = new HBox(senteClockView);

        // Bind clock widths to the board view width
        goteClockView.prefWidthProperty().bind(boardView.widthProperty().divide(8));
        senteClockView.prefWidthProperty().bind(boardView.widthProperty().divide(8));

        // Align the clocks to the right
        goteClockWrapper.setAlignment(Pos.CENTER_LEFT);
        senteClockWrapper.setAlignment(Pos.CENTER_RIGHT);

        // Bind max width of clock wrappers to the board width
        goteClockWrapper.maxWidthProperty().bind(boardView.widthProperty());
        senteClockWrapper.maxWidthProperty().bind(boardView.widthProperty());

        // Add the clock and board wrapper components
        clockAndBoardWrapper.getChildren().addAll(goteClockWrapper, boardWrapper, senteClockWrapper);
        clockAndBoardWrapper.prefHeightProperty().bind(hBox.heightProperty());

        // Bind minimum width for piece stand views
        gotePieceStandView.minWidthProperty().bind(boardView.widthProperty().divide(boardSize));
        sentePieceStandView.minWidthProperty().bind(boardView.widthProperty().divide(boardSize));

        // Bind history view height to the board wrapper height
        historyView.maxHeightProperty().bind(boardWrapper.heightProperty());

        // Add all components to the main HBox
        hBox.getChildren().addAll(clockAndBoardWrapper, historyView);

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
