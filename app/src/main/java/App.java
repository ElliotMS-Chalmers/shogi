import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.Game;
import variants.Standard;
import variants.Variant;
import view.MainView;

public class App extends Application {
	public void start(Stage stage) {
		Variant gameMode = new Standard();
		Game game = new Game(gameMode);


		MainView mainView = new MainView(); 
		Scene scene = new Scene(mainView.getView(), 1280, 1000);
		
		stage.setTitle("Shogi v1.0");
		stage.setScene(scene);
		stage.show();
	}

    public static void main(String[] args) {
		launch(args);
    }
}
