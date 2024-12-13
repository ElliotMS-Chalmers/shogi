package view;

import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;

public class MainView extends BorderPane {
	ShogiView shogiView;

	public MainView(ShogiView shogiView, GameMenu gameMenu, SettingsMenu settingsMenu) {
		this.shogiView = shogiView;
		this.setCenter(shogiView);
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(gameMenu, settingsMenu);
		this.setTop(menuBar);
	}
}
