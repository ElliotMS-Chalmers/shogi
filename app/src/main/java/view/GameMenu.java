package view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.function.Consumer;
import javafx.event.Event;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GameMenu extends Menu {
    private Consumer<String> onDialogResult;
    private Consumer<String> onFileChooserResult;
    private Consumer<String> onFileSaverResult;

    public GameMenu() {
        this.setText("Game");

        MenuItem newGameMenuItem = new MenuItem("New Game ");
        MenuItem loadGameMenuItem = new MenuItem("Load Game...");
        MenuItem saveGameMenuItem = new MenuItem("Save Game As...");

        newGameMenuItem.setOnAction(this::showDialog);
        loadGameMenuItem.setOnAction(this::openFileChooser);
        saveGameMenuItem.setOnAction(this::openFileSaver);

        this.getItems().addAll(
                newGameMenuItem,
                loadGameMenuItem,
                saveGameMenuItem
        );
    }

    public void setOnDialogResult(Consumer<String> callback) {
        this.onDialogResult = callback;
    }
    public void setOnFileChooserResult(Consumer<String> callback) {
        this.onFileChooserResult = callback;
    }
    public void setOnFileSaverResult(Consumer<String> callback) {
        this.onFileSaverResult = callback;
    }

    private void showDialog(Event e) {
        Dialog<String> dialog = new Dialog<>();
        Scene currentScene = ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow().getScene();
        dialog.getDialogPane().getStylesheets().addAll(currentScene.getStylesheets());
        dialog.getDialogPane().setPrefSize(250, 150);
        dialog.setTitle("Create new game");
        // dialog.setHeaderText("Create new game");


        Label variantLabel = new Label("Variant:");
        MenuButton variantMenuButton = new MenuButton("Standard");
        HBox variantContainer = new HBox(5, variantLabel, variantMenuButton);
        variantContainer.setAlignment(Pos.CENTER);
        MenuItem standard = new MenuItem("Standard");
        MenuItem minishogi = new MenuItem("Minishogi");
//        MenuItem chushogi = new MenuItem("Chushogi (todo)");
//        MenuItem annanshogi = new MenuItem("Annanshogi (todo)");
//        MenuItem kyotoshogi = new MenuItem("Kyotoshogi (todo)");
//        MenuItem checkshogi = new MenuItem("Checkshogi (todo)");
        variantMenuButton.getItems().addAll(standard, minishogi/*, chushogi, annanshogi, kyotoshogi, checkshogi */);
        for (MenuItem item : variantMenuButton.getItems()) {
            item.setOnAction(event -> {
                variantMenuButton.setText(item.getText());
            });
        }

        TextFlow timeLabelFlow = new TextFlow();
        Text timeLabelText = new Text("Minutes per side: ");
        Text timeNumberText = new Text("0");;
        timeNumberText.setStyle("-fx-font-weight: bold");
        timeLabelFlow.getChildren().addAll(timeLabelText, timeNumberText);
        Slider timePerSideSlider = new Slider(0, 60,0);
        timePerSideSlider.setBlockIncrement(1);
        timePerSideSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            timeNumberText.setText(String.format("%.0f", newValue.doubleValue()));
        });
        VBox timePerSideContainer = new VBox(5, timeLabelFlow, timePerSideSlider);

        // Layout the controls in a grid
        VBox content = new VBox(10, variantContainer, timePerSideContainer /*, toggleGroupContainer */);
        content.setAlignment(Pos.TOP_CENTER);
        dialog.getDialogPane().setContent(content);

        // Add OK and Cancel buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handle the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return "Variant: " + variantMenuButton.getText() +
                       ", Time: " + (int) timePerSideSlider.getValue();
                        // ", Side: " + ((ToggleButton) sideToggleGroup.getSelectedToggle()).getText();
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

    private void openFileChooser(Event e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Save file", "*.json"));
        MenuItem menuItem = (MenuItem) e.getSource();
        Window window = menuItem.getParentPopup().getOwnerWindow();
        File selectedFile = fileChooser.showOpenDialog(window);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            System.out.println("Loaded game from: " + filePath);
            if (onFileChooserResult != null) {
                onFileChooserResult.accept(filePath);
            }
        }
    }

    private void openFileSaver(Event e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Save file", "*.json"));
        fileChooser.setInitialFileName("shogi_game_save.json");
        MenuItem menuItem = (MenuItem) e.getSource();
        Window window = menuItem.getParentPopup().getOwnerWindow();
        File selectedFile = fileChooser.showSaveDialog(window);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            if (onFileSaverResult != null) {
                onFileSaverResult.accept(filePath);
            }
        }
    }
}
