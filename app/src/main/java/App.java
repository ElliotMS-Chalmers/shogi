import controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.Game;
import model.Settings;
import model.variants.Standard;
import model.variants.Variant;
import view.*;
import view.views.GameView;

public class App extends Application {
	public void start(Stage stage) {
        // Load settings
        Settings settings = new Settings("/settings.properties");
        ThemeManager themeManager = new ThemeManager();
        Theme theme = new Theme(
                themeManager.getPieceSet(settings.getPieceSet()),
                themeManager.getBoardTheme(settings.getBoardTheme())
        );

        // Initialize model
        Variant variant = new Standard();
        Game model = new Game(variant);

        // Initialize view
        GameView gameView = new GameView(theme); // should move theme to model
        Scene scene = new Scene(gameView, 1280, 1000);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Shogi v1.0");
        stage.setScene(scene);
        stage.setMinHeight(480);
        stage.setMinWidth(720);
        stage.show();

        // Initialize controller
        GameController gameController = new GameController(model, gameView);
    }

    public static void main(String[] args) {
		launch(args);
    }
}
