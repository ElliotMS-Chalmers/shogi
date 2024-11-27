package view.views;

import controller.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import util.Sfen;
import util.Side;
import view.Theme;

public class GameView extends BorderPane {
	private BoardView boardView;
	private PieceStandView gotePieceStandView;
	private PieceStandView sentePieceStandView;
	private final Theme theme;
	private GameController controller;

	public GameView(Theme theme) {
		this.theme = theme;

		this.boardView = new BoardView(9, theme);
		this.gotePieceStandView = new PieceStandView(Side.GOTE, theme);
		this.sentePieceStandView = new PieceStandView(Side.SENTE, theme);

		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(10);
		hBox.getChildren().addAll(gotePieceStandView, boardView, sentePieceStandView);

		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		VBox.setVgrow(hBox, Priority.ALWAYS);
		vBox.getChildren().add(hBox);
		VBox.setMargin(hBox, new Insets(100, 0, 100, 0));

		// root.getStyleClass().add("dark");
		this.setCenter(vBox);
	}

	public void clearBoard() {
		boardView.clearBoard();
	}

	public void drawBoard(Sfen sfen) {
		boardView.drawBoard(sfen);
	}

	public void drawHand(Sfen sfen) {
		gotePieceStandView.drawHand(sfen);
		sentePieceStandView.drawHand(sfen);
	}

	public void highlightSquare(util.Pos pos) {
		boardView.highlightSquare(pos);
	}

	public void clearHighlightedSquares() {
		boardView.clearHighlightedSquares();
	}

	public void setController(GameController controller) {
		boardView.setClickHandler(controller::processBoardClick);
		gotePieceStandView.setClickHandler(controller::processHandClick);
		sentePieceStandView.setClickHandler(controller::processHandClick);
	}
}
