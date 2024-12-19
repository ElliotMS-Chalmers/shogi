package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SettingsMenu extends Menu {
    private final Menu boardThemeMenu = new Menu("Board Theme");
    private final Menu pieceSetMenu = new Menu("Piece Set");
    private final Menu soundMenu = new Menu("Sound");

    public SettingsMenu() {
        this.setText("Settings");

        this.getItems().addAll(
                boardThemeMenu,
                pieceSetMenu,
                soundMenu
        );
    }

    public void addBoardThemeMenuItem(String name, Image image, EventHandler<ActionEvent> handler) {
        MenuItem menuItem = createMenuItem(name, image);
        menuItem.setOnAction(handler);
        boardThemeMenu.getItems().add(menuItem);
    }

    public void addPieceSetSubMenu(String subMenuName) {
        Menu menu = new Menu(subMenuName);
        pieceSetMenu.getItems().add(menu);
    }

    public void addPieceSetSubMenuItem(String subMenuName, String menuItemName, Image image, EventHandler<ActionEvent> handler) {
        Menu subMenu = pieceSetMenu.getItems().stream()
                .filter(menu -> menu.getText().equals(subMenuName))
                .map(menu -> (Menu) menu)
                .findFirst()
                .orElse(null);

        if (subMenu != null) {
            MenuItem menuItem = createMenuItem(menuItemName, image);
            menuItem.setOnAction(handler);
            subMenu.getItems().add(menuItem);
        }
    }

    public void addSoundMenuItem(String name, EventHandler<ActionEvent> handler) {
        MenuItem menuItem = createMenuItem(name, null);
        menuItem.setOnAction(handler);
        soundMenu.getItems().add(menuItem);
    }

    public MenuItem createMenuItem(String name, Image image) {
        MenuItem menuItem = new MenuItem(name);
        if (image != null) {
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            menuItem.setGraphic(imageView);
        }
        return menuItem;
    }
}
