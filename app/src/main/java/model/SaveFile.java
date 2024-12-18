package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import model.pieces.Piece;
import model.settings.JsonLoader;
import model.variants.Mini;
import model.variants.Standard;
import model.variants.Variant;
import util.PathResolver;
import util.Pos;
import util.Side;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Represents a save file containing the state of a Shogi game.
 * Provides methods to load, save, and serialize the state of the game.
 */
public class SaveFile {
    private final String sfen;
    private final List<Move> history;
    private final String variant;
    private final Map<Side, Integer> timeLeft;

    @JsonCreator
    public SaveFile(
            @JsonProperty("sfen") String sfen,
            @JsonProperty("history") List<Move> history,
            @JsonProperty("variant") String variant,
            @JsonProperty("timeLeft") Map<Side, Integer> timeLeft
    ) {
        this.sfen = sfen;
        this.history = history;
        this.variant = variant;
        this.timeLeft = timeLeft;
    }

    public SaveFile(Game game) {
        this.sfen = game.getSfen().toString();
        this.history = game.getHistory().serialize();
        this.variant = game.getVariant().serialize();
        this.timeLeft = new HashMap<>();
        timeLeft.put(Side.SENTE, game.getTime(Side.SENTE));
        timeLeft.put(Side.GOTE, game.getTime(Side.GOTE));
    }

    public static SaveFile load() {
        return load(getSaveFilePath().toString());
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
        return PathResolver.getAppDataPath("save.json");
    }

    @JsonIgnore
    public Sfen getSfen() {
        return new Sfen(this.sfen);
    }

    @JsonGetter("sfen")
    public String getSerializedSfen() {
        return sfen;
    }

    @JsonIgnore
    public History getHistory() {
        History historyObj = new History();
        if (history == null) return historyObj;
        for (Move move : history) {
            historyObj.addMove(move);
        }
        return historyObj;
    }

    @JsonGetter("history")
    public List<Move> getSerializedHistory() {
        return this.history;
    }

    public Variant getVariant() {
        return switch (variant) {
            case "Standard" -> new Standard();
            case "Mini" -> new Mini();
            default -> throw new IllegalArgumentException("Unsupported variant in save file: " + variant);
        };
    }

    @JsonIgnore
    public int getTime(Side side) {
        return timeLeft.get(side);
    }

    @JsonGetter("timeLeft")
    public Map<Side, Integer> getSerializedTimeLeft() {
        return timeLeft;
    }
}
