package view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class GameMenu extends Menu {
    public GameMenu() {
        this.setText("Game");

        MenuItem newGameMenuItem = new MenuItem("New Game ");
        MenuItem loadGameMenuItem = new MenuItem("Load Game...");
        MenuItem saveGameMenuItem = new MenuItem("Save Game As...");

        newGameMenuItem.setOnAction(e -> {
           showDialog();
        });

        this.getItems().addAll(
                newGameMenuItem,
                loadGameMenuItem,
                saveGameMenuItem
        );
    }

    private void showDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Advanced Dialog");
        dialog.setHeaderText("Select your options:");

        // Create the form controls
        RadioButton option1 = new RadioButton("Option 1");
        RadioButton option2 = new RadioButton("Option 2");
        ToggleGroup group = new ToggleGroup();
        option1.setToggleGroup(group);
        option2.setToggleGroup(group);

        Slider slider = new Slider(0, 100, 50);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        CheckBox checkBox = new CheckBox("Enable feature");

        // Layout the controls in a grid
        VBox content = new VBox(10, option1, option2, slider, checkBox);
        dialog.getDialogPane().setContent(content);

        // Add OK and Cancel buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handle the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return "Selected: " +
                        (option1.isSelected() ? "Option 1" : "Option 2") +
                        ", Slider: " + (int) slider.getValue() +
                        ", Feature Enabled: " + checkBox.isSelected();
            }
            return null;
        });

        // Show the dialog and process the result
        dialog.showAndWait().ifPresent(result -> {
            System.out.println("Result: " + result);
        });
    }
}
