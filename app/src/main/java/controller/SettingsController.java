package controller;

import javafx.scene.control.MenuItem;
import model.Settings;
import view.SettingsMenu;

public class SettingsController {
    private final Settings settings;
    private final SettingsMenu settingsMenu;

    public SettingsController(Settings settings, SettingsMenu settingsMenu) {
        this.settings = settings;
        this.settingsMenu = settingsMenu;

        populateBoardThemeMenu();
        populatePieceSetMenu();
    }

    private void populateBoardThemeMenu() {
        settings.getBoardThemes().forEach((name, set) -> {
            MenuItem menuItem = new MenuItem(name);
            settingsMenu.addBoardThemeMenuItem(menuItem);
            menuItem.setOnAction(e -> {
                settings.setBoardTheme(name);
            });
        });
    }

    private void populatePieceSetMenu() {
        settings.getPieceSets().forEach((name, set) -> {
            MenuItem menuItem = new MenuItem(name);
            settingsMenu.addPieceSetMenuItem(menuItem);
            menuItem.setOnAction(e -> {
                settings.setPieceSet(name);
            });
        });
    }
}
