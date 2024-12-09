package view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
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
        this.getChildren().addAll(gotePieceStandView, boardView, sentePieceStandView, historyView);
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
