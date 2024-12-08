package view;

import javafx.scene.control.*;

public class SettingsMenu extends Menu {
    // private final Menu backgroundMenu = new Menu("Background");
    private final Menu boardThemeMenu = new Menu("Board theme");
    private final Menu pieceSetMenu = new Menu("Piece set");
    private final Menu soundMenu = new Menu("Sound");
    // private final Menu notationMenu = new Menu("Notation");

    public SettingsMenu() {
        this.setText("Settings");

        // backgroundMenu.getItems().addAll(
        //        new MenuItem("Light"),
        //        new MenuItem("Dark")
        // );

        this.getItems().addAll(
                // backgroundMenu,
                boardThemeMenu,
                pieceSetMenu,
                soundMenu
                // notationMenu
        );
    }

    public void addBoardThemeMenuItem(MenuItem item) {
        boardThemeMenu.getItems().add(item);
    }

    public void addPieceSetMenuItem(MenuItem item) {
        pieceSetMenu.getItems().add(item);
    }

    public void addSoundMenuItem(MenuItem item) {
        soundMenu.getItems().add(item);
    }
}
