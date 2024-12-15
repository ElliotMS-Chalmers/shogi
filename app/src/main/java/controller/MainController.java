package controller;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.util.Duration;
import model.Game;
import model.SaveFile;
import model.settings.Settings;
import model.variants.Standard;
import view.MainView;

public class MainController {
    Game game;
    private final Settings settings;
    private final MenuController menuController;
    private ShogiController shogiController;
    private final SettingsController settingsController;
    private MainView mainView;
    private final Scene scene;

    public MainController() {
        SaveFile saveFile = SaveFile.load();
        if (saveFile != null) {
            this.game = new Game(saveFile);
        } else {
            this.game = new Game(new Standard(), 0);
        }
        this.settings = new Settings();

        menuController = new MenuController();
        shogiController = new ShogiController(settings, game);
        settingsController = new SettingsController(settings);

        mainView = new MainView(shogiController.getView(), menuController.getMenu(), settingsController.getMenu());
        scene = new Scene(mainView, 1280, 720);

        menuController.setNewGameHandler(this::newGame);
        menuController.setSaveGameHandler(this::saveGame);
    }

    public Scene getScene() {
        return scene;
    }

    public void shutdown() {
        game.stopClock();
        SaveFile saveFile = new SaveFile(game);
        saveFile.save();
    }

    private void newGame(Game game) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(100), scene.getRoot());
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(event -> {
            this.game.stopClock();
            this.game = game;
            this.shogiController = new ShogiController(settings, game);
            this.mainView.setShogiView(shogiController.getView());

            FadeTransition fadeIn = new FadeTransition(Duration.millis(100), scene.getRoot());
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });

        fadeOut.play();
    }

    private void saveGame(String path) {
        SaveFile saveFile = new SaveFile(game);
        saveFile.save(path);
    }
}
