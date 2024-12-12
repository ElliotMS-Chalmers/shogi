package controller;

import javafx.scene.Scene;
import model.Game;
import model.Settings;
import model.variants.Standard;
import view.MainView;

public class MainController {
    Game game;
    private final Settings settings;
    private final DialogController dialogController;
    private ShogiController shogiController;
    private final SettingsController settingsController;
    private MainView mainView;
    private final Scene scene;

    public MainController() {
        this.game = new Game(new Standard(), 0); // Default game on launch, should be replaced with saved game
        this.settings = new Settings();

        dialogController = new DialogController();
        shogiController = new ShogiController(settings, game);
        settingsController = new SettingsController(settings);

        mainView = new MainView(shogiController.getView(), dialogController.getMenu(), settingsController.getMenu());
        scene = new Scene(mainView, 1280, 720);

        dialogController.setNewGameHandler(this::newGame);
    }

    public Scene getScene() {
        return scene;
    }

    public void shutdown() {
        game.stopClock();
    }

    private void newGame(Game game) {
        this.game.stopClock();
        this.game = game;
        this.shogiController = new ShogiController(settings, game);
        this.mainView = new MainView(shogiController.getView(), dialogController.getMenu(), settingsController.getMenu());
        scene.setRoot(mainView);
    }
}
