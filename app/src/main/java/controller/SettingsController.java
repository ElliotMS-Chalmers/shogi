package controller;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import model.pieces.chu.CopperGeneral;
import model.settings.PieceSetType;
import model.settings.Settings;
import model.pieces.GoldGeneral;
import model.pieces.Lance;
import model.pieces.Piece;
import util.Side;
import view.SettingsMenu;

import java.io.InputStream;

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
            settingsMenu.addBoardThemeMenuItem(name, new Image(theme.getImage()), (e) -> settings.setBoardTheme(name));
        });
    }

    private void populatePieceSetMenu() {
        settings.getPieceSets().forEach((type, sets) -> {
            Piece piece = switch (type) {
                case PieceSetType.STANDARD -> new GoldGeneral(Side.SENTE);
                case PieceSetType.CHU -> new CopperGeneral(Side.SENTE);
                case PieceSetType.KYO -> new Lance(Side.SENTE);
            };
            String subMenuName = type.toString();
            settingsMenu.addPieceSetSubMenu(subMenuName);
            sets.forEach((name, set) -> {
                settingsMenu.addPieceSetSubMenuItem(
                        subMenuName,
                        name,
                        new Image(set.getImage(piece)),
                        e -> { settings.setPieceSet(type, name); }
                );
            });
        });
    }

    private void populateSoundMenu() {
        settings.getSoundSets().forEach((name, set) -> {
            settingsMenu.addSoundMenuItem(name, (e) -> settings.setSoundSet(name));
        });
    }

    public SettingsMenu getMenu() {
        return settingsMenu;
    }
}
