package view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import util.Side;

public class ShogiView extends HBox {
    private final BoardView boardView;
    private final PieceStandView gotePieceStandView;
    private final PieceStandView sentePieceStandView;
    //private final HistoryView historyView;

    public ShogiView() {
        this.boardView = new BoardView(9); // size hard coded for now
        this.gotePieceStandView = new PieceStandView(Side.GOTE);
        this.sentePieceStandView = new PieceStandView(Side.SENTE);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.getChildren().addAll(gotePieceStandView, boardView, sentePieceStandView);
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
}
