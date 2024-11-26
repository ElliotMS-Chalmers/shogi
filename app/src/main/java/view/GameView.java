package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

public class GameView {
	private final BorderPane root = new BorderPane();

	public GameView(BoardView board, PieceStandView leftPieceStand, PieceStandView rightPieceStand) {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(10);
		hBox.getChildren().addAll(leftPieceStand.getView(), board.getView(), rightPieceStand.getView());

		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		VBox.setVgrow(hBox, Priority.ALWAYS);
		vBox.getChildren().add(hBox);
		VBox.setMargin(hBox, new Insets(100, 0, 100, 0));

		// root.getStyleClass().add("dark");

		root.setCenter(vBox);
	}

	public BorderPane getView() {
		return root;
	}
}
