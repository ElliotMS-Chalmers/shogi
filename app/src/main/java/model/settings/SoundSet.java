package model.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SoundSet {
    private final String directory;

    @JsonCreator
    public SoundSet(@JsonProperty("directory") String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public String getMoveSoundPath() {
        return getAbsolutePath("Move.mp3");
    }

    public String getCaptureSoundPath() {
        return getAbsolutePath("Capture.mp3");
    }

    private String getAbsolutePath(String file) {
        return getClass().getResource(String.format("/sound/%s/%s", directory, file)).toString();
    }
}

