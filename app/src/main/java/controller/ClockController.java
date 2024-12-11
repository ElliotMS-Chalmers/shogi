package controller;

import model.Game;
import view.GameMenu;

public class ClockController {

    private Game game;
    private GameMenu gameMenuView;

    public ClockController(Game game, GameMenu gameMenu) {
        this.game = game;
        this.gameMenuView = gameMenu;

        gameMenuView.setOnDialogResult(this::handleDialogResult);
    }

    private void handleDialogResult(String result) {
        System.out.println("Dialog result received: " + result);
        // Parse the dialog result
        if (result != null) {
            String[] parts = result.split(", ");
            for (String part : parts) {
                if (part.startsWith("Spinner:")) { // Handle Spinner value for clocks
                    int spinnerValue = Integer.parseInt(part.split(":")[1].trim());
                    System.out.println("Spinner Value: " + spinnerValue);

                    // Use the spinner value (e.g., set clock time)
                    game.setClocks(spinnerValue * 60); // Convert minutes to seconds
                    game.startClocks();
                }
            }
        }
    }
}
