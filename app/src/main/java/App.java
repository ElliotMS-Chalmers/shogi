import controller.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	public void start(Stage stage) {
        MainController mainController = new MainController();
        Scene scene = mainController.getScene();
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Shogi v1.0");
        stage.setScene(scene);
        stage.setMinHeight(480);
        stage.setMinWidth(720);
        stage.show();

        stage.setOnCloseRequest(event -> {
            System.out.println("Closing the application...");
            mainController.shutdown();
        });
    }

    public static void main(String[] args) {
		launch(args);
    }
}
