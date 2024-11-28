package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

public class GameView extends BorderPane {

	public GameView(ShogiView shogiView) {
		// maybe move these into shogi view?
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		VBox.setVgrow(shogiView, Priority.ALWAYS);
		vBox.getChildren().add(shogiView);
		VBox.setMargin(shogiView, new Insets(100, 0, 100, 0));

		// root.getStyleClass().add("dark");
		this.setCenter(vBox);
	}
}
