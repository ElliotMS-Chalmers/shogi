package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;

public class GameView extends BorderPane {

	public GameView(ShogiView shogiView, SettingsMenu settingsMenu) {
		// maybe move these into shogi view?
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		VBox.setVgrow(shogiView, Priority.ALWAYS);
		vBox.getChildren().add(shogiView);
		VBox.setMargin(shogiView, new Insets(100, 0, 100, 0));
		this.setCenter(vBox);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(settingsMenu);
		this.setTop(menuBar);

		// root.getStyleClass().add("dark");
	}
}
