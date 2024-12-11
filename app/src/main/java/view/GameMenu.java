package view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.util.function.Consumer;

public class GameMenu extends Menu {
    private Consumer<String> onDialogResult; //callback for dialog result
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

    public void setOnDialogResult(Consumer<String> callback) {
        this.onDialogResult = callback;
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

        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,20,0,1);
        spinner.setValueFactory(valueFactory);

        CheckBox checkBox = new CheckBox("Enable feature");

        // Layout the controls in a grid
        VBox content = new VBox(10, option1, option2, spinner, checkBox);
        dialog.getDialogPane().setContent(content);

        // Add OK and Cancel buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handle the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return "Selected: " +
                        (option1.isSelected() ? "Option 1" : "Option 2") +
                        ", Spinner: " + (int) spinner.getValue() +
                        ", Feature Enabled: " + checkBox.isSelected();
            }
            return null;
        });

        // Show the dialog and process the result
        dialog.showAndWait().ifPresent(result -> {
            if (onDialogResult != null) {
                onDialogResult.accept(result); //Notify the controller
            }
        });
    }
}
