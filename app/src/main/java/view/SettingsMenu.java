package view;

import javafx.scene.control.*;

public class SettingsMenu extends Menu {
    // private final Menu backgroundMenu = new Menu("Background");
    private final Menu boardThemeMenu = new Menu("Board Theme");
    private final Menu pieceSetMenu = new Menu("Piece Set");
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

    public void addPieceSetMenuMenu(Menu item) {
        pieceSetMenu.getItems().add(item);
    }

    public void addSoundMenuItem(MenuItem item) {
        soundMenu.getItems().add(item);
    }
}
