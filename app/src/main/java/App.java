import controller.ShogiController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.Game;
import model.Settings;
import model.variants.Standard;
import model.variants.Variant;
import view.GameView;
import view.ShogiView;

public class App extends Application {
	public void start(Stage stage) {
        // Initialize model
        Settings settings = new Settings("/settings.properties");
        Variant variant = new Standard();
        Game game = new Game(variant);

        // Initialize view
        ShogiView shogiView = new ShogiView();
        GameView gameView = new GameView(shogiView);
        Scene scene = new Scene(gameView, 1280, 1000);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Shogi v1.0");
        stage.setScene(scene);
        stage.setMinHeight(480);
        stage.setMinWidth(720);
        stage.show();

        // Initialize controller
        ShogiController shogiController = new ShogiController(settings, game, shogiView);
    }

    public static void main(String[] args) {
		launch(args);
    }
}
