import controller.SettingsController;
import controller.ShogiController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.Game;
import model.Settings;
import model.variants.Mini;
import model.variants.Standard;
import model.variants.Variant;
import view.GameView;

public class App extends Application {
	public void start(Stage stage) {
        // Initialize model
        Settings settings = new Settings();
        // Variant variant = new Standard();
        Variant variant = new Mini();
        Game game = new Game(variant);

        // Initialize controller (which also initializes respective subviews)
        ShogiController shogiController = new ShogiController(settings, game);
        SettingsController settingsController = new SettingsController(settings);

        // Initialize view
        GameView gameView = new GameView(shogiController.getView(), settingsController.getMenu());
        Scene scene = new Scene(gameView, 1280, 1000);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Shogi v1.0");
        stage.setScene(scene);
        stage.setMinHeight(480);
        stage.setMinWidth(720);
        stage.show();

        // Handle window close event
        stage.setOnCloseRequest(event -> {
          System.out.println("Closing the application...");
          shogiController.stopClock();
        });
    }

    public static void main(String[] args) {
		launch(args);
    }
}
