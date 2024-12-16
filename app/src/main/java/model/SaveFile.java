package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import model.settings.JsonLoader;
import model.variants.Mini;
import model.variants.Standard;
import model.variants.Variant;
import util.Side;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SaveFile {
    private final String sfen;
    private final List<String> history;
    private final Variant variant;
    private final Map<Side, Integer> timeLeft;

    @JsonCreator
    public SaveFile(
            @JsonProperty("sfen") String sfen,
            @JsonProperty("history") List<String> history,
            @JsonProperty("variant") Variant variant,
            @JsonProperty("timeLeft") Map<Side, Integer> timeLeft
    ) {
        this.sfen = sfen;
        this.history = history;
        this.variant = variant;
        this.timeLeft = timeLeft;
    }

    public SaveFile(Game game) {
        this.sfen = game.getSfen().toString();

        this.history = new ArrayList<>();
        for (Iterator<Move> it = game.getHistory().getMoves(); it.hasNext(); ) {
            Move move = it.next();
            history.add(move.toString());
        }

        this.variant = game.getVariant() instanceof model.variants.Standard ? Variant.STANDARD :
                       game.getVariant() instanceof model.variants.Mini ? Variant.MINI :
                       Variant.STANDARD;

        this.timeLeft = new HashMap<>();
        timeLeft.put(Side.SENTE, game.getTime(Side.SENTE));
        timeLeft.put(Side.GOTE, game.getTime(Side.GOTE));
    }

    public static SaveFile load() {
        return JsonLoader.load(getSaveFilePath().toString(), new TypeReference<SaveFile>() {});
    }

    public static SaveFile load(String path) {
        return JsonLoader.load(path, new TypeReference<SaveFile>() {});
    }

    public void save() {
        Path path = getSaveFilePath();
        try {
            if (path.getParent() != null) {
                java.nio.file.Files.createDirectories(path.getParent());
            }
            JsonLoader.getObjectMapper().writeValue(path.toFile(), this);
        } catch (IOException e) {
            System.err.println("Failed to save save file: " + e.getMessage());
        }
    }

    public void save(String p) {
        Path path = Paths.get(p);
        try {
            if (path.getParent() != null) {
                java.nio.file.Files.createDirectories(path.getParent());
            }
            JsonLoader.getObjectMapper().writeValue(path.toFile(), this);
        } catch (IOException e) {
            System.err.println("Failed to save save file: " + e.getMessage());
        }
    }

    private static Path getSaveFilePath() {
        String userHome = System.getProperty("user.home");
        String os = System.getProperty("os.name").toLowerCase();

        Path path;

        if (os.contains("win")) {
            path = Paths.get(userHome, "AppData", "Local", "Shogi", "save.json");
        } else {
            path = Paths.get(userHome, ".config", "Shogi", "save.json");
        }
        return path;
    }

    @JsonIgnore
    public Sfen getSfen() {
        return new Sfen(sfen);
    }

    @JsonIgnore
    public History getHistory() {
        return new History();
    }

    private enum Variant {
        STANDARD,
        MINI,
        CHU,
        KYO
    }
    @JsonIgnore
    public model.variants.Variant getVariant() {
        return switch (variant) {
            case STANDARD -> new Standard();
            case MINI -> new Mini();
            default -> new Standard();
        };
    }

    @JsonIgnore
    public int getTime(Side side) {
        return timeLeft.get(side);
    }

    @JsonGetter("sfen")
    public String getSerializedSfen() {
        return sfen;
    }

    @JsonGetter("history")
    public List<String> getSerializedHistory() {
        return history;
    }

    @JsonGetter("variant")
    public String getSerializedVariant() {
        return variant.toString();
    }

    @JsonGetter("timeLeft")
    public Map<Side, Integer> getSerializedTimeLeft() {
        return timeLeft;
    }
}
