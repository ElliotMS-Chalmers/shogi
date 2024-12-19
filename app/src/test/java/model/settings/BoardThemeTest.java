package model.settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class BoardThemeTest {
    private BoardTheme boardTheme;

    @BeforeEach
    void setUp() {
        boardTheme = new BoardTheme("doubutsu.png", "doubutsu.png");
    }

    @Test
    void constructorShouldInitializeFields() {
        assertEquals("doubutsu.png", boardTheme.getImageName());
        assertEquals("doubutsu.png", boardTheme.getThumbnailName());
    }

    @Test
    void getBackgroundShouldReturnInputStreamForValidResource() {
        InputStream inputStream = boardTheme.getBackground();
        assertNotNull(inputStream, "InputStream should not be null for a valid resource.");
        try {
            inputStream.close();
        } catch (Exception e) {
            fail("Failed to close InputStream: " + e.getMessage());
        }
    }

    @Test
    void getBackgroundShouldThrowExceptionForInvalidResource() {
        BoardTheme invalidTheme = new BoardTheme("nonexistentImage.png", "thumbnail.png");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, invalidTheme::getBackground);
        assertTrue(exception.getMessage().contains("Resource not found"), "Exception message should contain 'Resource not found'.");
    }
}