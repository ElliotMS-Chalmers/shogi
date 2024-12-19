package model.settings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

/**
 * Unit tests for the SoundSet class.
 */
public class SoundSetTest {
    private String directory;
    private SoundSet soundSet;

    @BeforeEach
    void setUp() {
        directory = "shogi";
        soundSet = new SoundSet(directory);
    }

    @Test
    public void testConstructorAndGetDirectory() {
        assertEquals(directory, soundSet.getDirectory(), "The directory should match the expected value.");
    }

    @Test
    public void testGetMoveSoundPath() {
        assertTrue(soundSet.getMoveSoundPath().endsWith("Move.mp3"), "The move sound path should end with 'Move.mp3'.");
    }

    @Test
    public void testGetCaptureSoundPath() {
        assertTrue(soundSet.getCaptureSoundPath().endsWith("Capture.mp3"), "The capture sound path should end with 'Capture.mp3'.");
    }
}
