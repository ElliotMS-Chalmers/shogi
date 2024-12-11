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
        dialog.setTitle("Create new game");
        // dialog.setHeaderText("Create new game");

        MenuButton variantMenuButton = new MenuButton("Variant");
        MenuItem standard = new MenuItem("Standard");
        MenuItem minishogi = new MenuItem("Minishogi");
        MenuItem chushogi = new MenuItem("Chushogi (todo)");
        MenuItem annanshogi = new MenuItem("Annanshogi (todo)");
        MenuItem kyotoshogi = new MenuItem("Kyotoshogi (todo)");
        MenuItem checkshogi = new MenuItem("Checkshogi (todo)");
        variantMenuButton.getItems().addAll(standard, minishogi, chushogi, annanshogi, kyotoshogi, checkshogi);
        for (MenuItem item : variantMenuButton.getItems()) {
            item.setOnAction(event -> {
                variantMenuButton.setText(item.getText());
            });
        }

        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,20,0,1);
        spinner.setValueFactory(valueFactory);

        ToggleGroup sideToggleGroup = new ToggleGroup();
        ToggleButton sente = new ToggleButton("Sente");
        ToggleButton random = new ToggleButton("Random");
        ToggleButton gote = new ToggleButton("Gote");
        sente.setToggleGroup(sideToggleGroup);
        random.setToggleGroup(sideToggleGroup);
        gote.setToggleGroup(sideToggleGroup);
        random.setSelected(true);

        // Layout the controls in a grid
        VBox content = new VBox(10, variantMenuButton, spinner, sente, random, gote);
        dialog.getDialogPane().setContent(content);

        // Add OK and Cancel buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handle the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return "Variant: " + variantMenuButton.getText() +
                       ", Spinner: " + (int) spinner.getValue() +
                        ", Side: " + ((ToggleButton) sideToggleGroup.getSelectedToggle()).getText();
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
