package model.settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.PathResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    private Settings settings;

    @BeforeEach
    void setUp() {
        // Create a new Settings instance before each test
        settings = new Settings();
    }

    @Test
    void testLoadSettingsFromFile() {
        // Test that settings are loaded from a file
        assertNotNull(settings.getStandardPieceSet());
        assertNotNull(settings.getChuPieceSet());
        assertNotNull(settings.getKyoPieceSet());
        assertNotNull(settings.getBoardTheme());
        assertNotNull(settings.getSoundSet());
    }

    @Test
    void testSaveSettings() throws IOException {
        // Test saving settings and ensuring the file exists
        Path settingsFile = PathResolver.getAppDataPath("/settings.properties");

        // Before saving, delete any existing file to test saving
        if (Files.exists(settingsFile)) {
            Files.delete(settingsFile);
        }

        // Change a setting and save
        settings.setBoardTheme("DOUBUTSU_FLIP");
        settings.save();

        // Check that the settings file was created and contains the correct properties
        assertTrue(Files.exists(settingsFile));
        assertTrue(Files.readAllLines(settingsFile).stream().anyMatch(line -> line.contains("DOUBUTSU_FLIP")));
    }

    @Test
    void testSetBoardTheme() {
        // Test setting a new board theme
        String newBoardThemeName = "DOUBUTSU";
        settings.setBoardTheme(newBoardThemeName);

        // Check if the board theme has been updated
        assertEquals(newBoardThemeName, settings.getBoardThemeName());
    }

    @Test
    void testSetPieceSet() {
        // Test setting a new piece set
        String newPieceSetName = "RYOKO_1_KANJI";
        settings.setPieceSet(PieceSetType.STANDARD, newPieceSetName);

        // Check if the piece set has been updated
        assertEquals(newPieceSetName, settings.getStandardPieceSetName());
    }

    @Test
    void testGetPieceSets() {
        // Test retrieving all available piece sets
        Map<PieceSetType, Map<String, PieceSet>> pieceSets = settings.getPieceSets();

        // Check if the piece sets for each type are non-empty
        assertTrue(pieceSets.containsKey(PieceSetType.STANDARD));
        assertTrue(pieceSets.containsKey(PieceSetType.CHU));
        assertTrue(pieceSets.containsKey(PieceSetType.KYO));
    }

    @Test
    void testGetBoardThemes() {
        // Test retrieving all available board themes
        Map<String, BoardTheme> boardThemes = settings.getBoardThemes();

        // Check that board themes are available
        assertFalse(boardThemes.isEmpty());
    }

//    @Test
//    void testSetSoundSet() {
//        // Test setting a new sound set
//        String newSoundSetName = "NewSoundSet";
//        settings.setSoundSet(newSoundSetName);
//
//        // Check if the sound set has been updated
//        assertEquals(newSoundSetName, settings.getSoundSetName());
//    }
}
