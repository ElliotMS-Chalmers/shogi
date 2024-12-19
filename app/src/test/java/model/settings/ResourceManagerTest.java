package model.settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class ResourceManagerTest {

    private ResourceManager resourceManager;

    @BeforeEach
    public void setUp() {
        // Initialize the resource manager
        resourceManager = new ResourceManager();
    }

    @Test
    public void testGetBoardThemes() {
        // Test if board themes are correctly loaded
        Map<String, BoardTheme> boardThemes = resourceManager.getBoardThemes();
        assertNotNull(boardThemes, "Board themes should not be null.");
        assertFalse(boardThemes.isEmpty(), "Board themes should not be empty.");
    }

    @Test
    public void testGetPieceSets() {
        // Test if piece sets are correctly loaded
        Map<PieceSetType, Map<String, PieceSet>> pieceSets = resourceManager.getPieceSets();
        String pieceTypeName = resourceManager.getPieceSetName(PieceSetType.STANDARD, resourceManager.getPieceSet(PieceSetType.STANDARD, "PORTELLA"));
        assertEquals("PORTELLA", pieceTypeName);
        assertNotNull(pieceSets, "Piece sets should not be null.");
        assertTrue(pieceSets.containsKey(PieceSetType.STANDARD), "Piece sets should contain STANDARD type.");
    }

    @Test
    public void testGetSoundSets() {
        // Test if sound sets are correctly loaded
        Map<String, SoundSet> soundSets = resourceManager.getSoundSets();
        assertNotNull(soundSets, "Sound sets should not be null.");
        assertTrue(soundSets.containsKey("SHOGI"), "Sound set 'shogi' should exist.");
    }

    @Test
    public void testGetBoardThemeNotFound() {
        // Test if the exception is thrown when the board theme is not found
        assertThrows(IllegalArgumentException.class, () -> {
            resourceManager.getBoardTheme("nonExistentTheme");
        });
    }

    @Test
    public void testGetBoardTheme() {
        assertNotNull(resourceManager.getBoardTheme("KAYA1"));

    }

    @Test
    public void testGetPieceSetNotFound() {
        // Test if the exception is thrown when the piece set is not found
        assertThrows(IllegalArgumentException.class, () -> {
            resourceManager.getPieceSet(PieceSetType.STANDARD, "nonExistentSet");
        });
    }

    @Test
    public void testGetSoundSetNotFound() {
        // Test if the exception is thrown when the sound set is not found
        assertThrows(IllegalArgumentException.class, () -> {
            resourceManager.getSoundSet("nonExistentSoundSet");
        });
    }

    @Test
    public void testGetSoundSet() {
        assertNotNull(resourceManager.getSoundSet("CHESS"));
    }

    @Test
    public void testGetBoardThemeName() {
        // Test if the method returns the correct board theme name
        BoardTheme boardTheme = resourceManager.getBoardThemes().get("OAK");
        String name = resourceManager.getBoardThemeName(boardTheme);
        assertEquals("OAK", name, "The board theme name should be 'theme1'.");
    }
}
