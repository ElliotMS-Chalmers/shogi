package controller;

import model.Game;
import model.SaveFile;
import model.variants.Mini;
import model.variants.Standard;
import model.variants.Variant;
import util.Side;
import view.GameMenu;

import java.util.function.Consumer;

public class MenuController {
    private GameMenu menu;
    private Consumer<Game> newGameHandler;
    private Consumer<String> saveGameHandler;

    public MenuController() {
        this.menu = new GameMenu();
        menu.setOnDialogResult(this::handleDialogResult);
        menu.setOnFileChooserResult(this::handleFileChooserResult);
        menu.setOnFileSaverResult(this::handleFileSaverResult);
    }

    private void handleDialogResult(String result) {
        System.out.println("Dialog result received: " + result);
        // Parse the dialog result
        if (result != null) {
            String[] parts = result.split(", ");
            Variant variant = new Standard();
            Integer time = null;
            Side side;
            for (String part : parts) {
                if (part.startsWith("Variant:")) {
                    variant = switch (part.substring("Variant: ".length())) {
                        case "Standard" -> new Standard();
                        case "Minishogi" -> new Mini();
                        default -> new Standard();
                    };
                } else if (part.startsWith("Time:")) { // Handle Spinner value for clocks
                    int spinnerValue = Integer.parseInt(part.split(":")[1].trim());
                    time = spinnerValue * 60;
                } else if (part.startsWith("Side:")) {
                    side = switch (part.substring("Side: ".length())) {
                        case "Sente" -> Side.SENTE;
                        case "Gote" -> Side.GOTE;
                        default -> null;
                    };
                }
            }

            if (newGameHandler != null) {
                Game newGame = new Game(variant, time);
                newGameHandler.accept(newGame);
            }
        }
    }

    private void handleFileChooserResult(String path) {
        SaveFile saveFile = SaveFile.load(path);
        Game game = new Game(saveFile);
        newGameHandler.accept(game);
    }

    private void handleFileSaverResult(String path) {
        saveGameHandler.accept(path);
    }

    public GameMenu getMenu() {
        return menu;
    }

    public void setNewGameHandler(Consumer<Game> newGameHandler) {
        this.newGameHandler = newGameHandler;
    }

    public void setSaveGameHandler(Consumer<String> saveGameHandler) {
        this.saveGameHandler = saveGameHandler;
    }
}
