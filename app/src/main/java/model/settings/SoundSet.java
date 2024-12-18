package model.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a set of sound effects for a Shogi game.
 * Each sound set contains sound effects for different in-game actions, such as moves and captures.
 */
public class SoundSet {

    /**
     * The directory containing the sound files for this sound set.
     */
    private final String directory;

    /**
     * Creates a new instance of {@code SoundSet} with the specified directory name.
     *
     * @param directory The directory containing the sound files.
     */
    @JsonCreator
    public SoundSet(@JsonProperty("directory") String directory) {
        this.directory = directory;
    }

    /**
     * Retrieves the directory name containing the sound files.
     *
     * @return The directory name.
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Retrieves the absolute path to the sound file for a move action.
     *
     * @return The absolute path to the "Move.mp3" sound file.
     */
    public String getMoveSoundPath() {
        return getAbsolutePath("Move.mp3");
    }

    /**
     * Retrieves the absolute path to the sound file for a capture action.
     *
     * @return The absolute path to the "Capture.mp3" sound file.
     */
    public String getCaptureSoundPath() {
        return getAbsolutePath("Capture.mp3");
    }

    /**
     * Constructs the absolute path to a specified sound file within this sound set's directory.
     *
     * @param file The name of the sound file.
     * @return The absolute path to the specified sound file.
     * @throws NullPointerException If the resource is not found.
     */
    private String getAbsolutePath(String file) {
        return getClass().getResource(String.format("/sound/%s/%s", directory, file)).toString();
    }
}
