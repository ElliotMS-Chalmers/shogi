import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import view.MainView;

public class App extends Application {
	public void start(Stage stage) {
		MainView mainView = new MainView(); 
		Scene scene = new Scene(mainView.getView(), 300, 200);
		
		stage.setTitle("Shogi v1.0");
		stage.setScene(scene);
		stage.show();
	}

    public static void main(String[] args) {
		launch(args);
    }
}
