package controller;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.PieceSetType;
import model.Settings;
import model.pieces.GoldGeneral;
import model.pieces.King;
import model.pieces.Piece;
import util.Side;
import view.SettingsMenu;

public class SettingsController {
    private final Settings settings;
    private final SettingsMenu settingsMenu;

    public SettingsController(Settings settings) {
        this.settings = settings;
        this.settingsMenu = new SettingsMenu();

        populateBoardThemeMenu();
        populatePieceSetMenu();
        populateSoundMenu();
    }

    private void populateBoardThemeMenu() {
        settings.getBoardThemes().forEach((name, theme) -> {
            MenuItem menuItem = createMenuItem(name, theme.getImage());
            settingsMenu.addBoardThemeMenuItem(menuItem);
            menuItem.setOnAction(e -> {
                settings.setBoardTheme(name);
            });
        });
    }

    private void populatePieceSetMenu() {
        // Piece piece = new GoldGeneral(Side.SENTE);
        settings.getPieceSets().forEach((type, sets) -> {
            Piece piece = switch (type) {
                case PieceSetType.STANDARD -> new GoldGeneral(Side.SENTE);
                case PieceSetType.CHU -> null;
                case PieceSetType.KYO -> null;
            };
            Menu menu = new Menu(type.toString());
            sets.forEach((name, set) -> {
                MenuItem menuItem = createMenuItem(name, set.getImage(piece)); //
                menu.getItems().add(menuItem);
                menuItem.setOnAction(e -> {
                    settings.setPieceSet(type, name);
                });
            });
            settingsMenu.addPieceSetMenuMenu(menu);
        });
    }

    private void populateSoundMenu() {
        settings.getSoundSets().forEach((name, set) -> {
            MenuItem menuItem = new MenuItem(name);
            settingsMenu.addSoundMenuItem(menuItem);
            menuItem.setOnAction(e -> {
                settings.setSoundSet(name);
            });
        });
    }

    private MenuItem createMenuItem(String name, Image image) {
        MenuItem menuItem = new MenuItem(name);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        menuItem.setGraphic(imageView);
        return menuItem;
    }

    public SettingsMenu getMenu() {
        return settingsMenu;
    }
}
