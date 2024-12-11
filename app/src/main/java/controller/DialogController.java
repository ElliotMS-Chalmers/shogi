package controller;

import model.Game;
import model.variants.Mini;
import model.variants.Standard;
import model.variants.Variant;
import util.Side;
import view.GameMenu;

import java.util.function.Consumer;

public class DialogController {
    private GameMenu menu;
    private Consumer<Game> newGameHandler;

    public DialogController() {
        this.menu = new GameMenu();
        menu.setOnDialogResult(this::handleDialogResult);
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
                System.out.println(part);
                if (part.startsWith("Variant:")) {
                    variant = switch (part.substring("Variant: ".length())) {
                        case "Standard" -> new Standard();
                        case "Minishogi" -> new Mini();
                        default -> new Standard();
                    };
                } else if (part.startsWith("Spinner:")) { // Handle Spinner value for clocks
                    int spinnerValue = Integer.parseInt(part.split(":")[1].trim());
                    time = spinnerValue * 60;
                    System.out.println("Spinner Value: " + spinnerValue);
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

    public GameMenu getMenu() {
        return menu;
    }

    public void setNewGameHandler(Consumer<Game> newGameHandler) {
        this.newGameHandler = newGameHandler;
    }
}
