package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import util.JsonLoader;
import model.variants.Mini;
import model.variants.Standard;
import model.variants.Variant;
import util.PathResolver;
import util.Side;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Represents a save file containing the state of a Shogi game.
 * This class provides methods to load, save, and serialize the state of the game.
 * <p>
 * The save file contains the following:
 * <ul>
 *   <li>The Shogi game state (SFEN notation)</li>
 *   <li>The history of moves made during the game</li>
 *   <li>The variant of the Shogi game being played</li>
 *   <li>The remaining time for each player</li>
 * </ul>
 */
public class SaveFile {

    /**
     * The SFEN (Shogi Forsyth-Edwards Notation) string representing the state of the game.
     */
    private final String sfen;

    /**
     * A list representing the history of moves made during the game.
     */
    private final List<Move> history;

    /**
     * The variant of the game (e.g., Standard, Mini).
     */
    private final String variant;

    /**
     * A map representing the remaining time for each player (Sente and Gote).
     */
    private final Map<Side, Integer> timeLeft;

    /**
     * Creates a new instance of SaveFile with the specified properties.
     *
     * @param sfen     The SFEN string representing the game state.
     * @param history  A list of moves made in the game.
     * @param variant  The variant of the Shogi game being played.
     * @param timeLeft A map of remaining time for each player (Sente and Gote).
     */
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

    /**
     * Creates a SaveFile instance from a Game object.
     *
     * @param game The game from which to create the SaveFile.
     */
    public SaveFile(Game game) {
        this.sfen = game.getSfen().toString();
        this.history = game.getHistory().serialize();
        this.variant = game.getVariant().serialize();
        this.timeLeft = new HashMap<>();
        timeLeft.put(Side.SENTE, game.getTime(Side.SENTE));
        timeLeft.put(Side.GOTE, game.getTime(Side.GOTE));
    }

    /**
     * Loads a save file from the default file path ("save.json").
     *
     * @return The loaded SaveFile object.
     */
    public static SaveFile load() {
        return load(getSaveFilePath().toString());
    }

    /**
     * Loads a save file from a specified file path.
     *
     * @param path The file path to load the save file from.
     * @return The loaded SaveFile object.
     */
    public static SaveFile load(String path) {
        return JsonLoader.load(path, new TypeReference<SaveFile>() {});
    }

    /**
     * Saves the current state of the SaveFile to the default file path ("save.json").
     */
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

    /**
     * Saves the current state of the SaveFile to a specified file path.
     *
     * @param p The file path to save the save file to.
     */
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

    /**
     * Returns the default file path for the save file ("save.json").
     *
     * @return The save file path.
     */
    private static Path getSaveFilePath() {
        return PathResolver.getAppDataPath("save.json");
    }

    /**
     * Retrieves the SFEN string representing the game state.
     *
     * @return The SFEN string.
     */
    @JsonIgnore
    public Sfen getSfen() {
        return new Sfen(this.sfen);
    }

    /**
     * Retrieves the serialized SFEN string to be used during JSON serialization.
     *
     * @return The SFEN string.
     */
    @JsonGetter("sfen")
    public String getSerializedSfen() {
        return sfen;
    }

    /**
     * Retrieves the history of moves made during the game.
     *
     * @return A History object containing the serialized list of moves.
     */
    @JsonIgnore
    public History getHistory() {
        History historyObj = new History();
        if (history == null) return historyObj;
        for (Move move : history) {
            historyObj.addMove(move);
        }
        return historyObj;
    }

    /**
     * Retrieves the serialized history of moves for JSON serialization.
     *
     * @return The list of moves.
     */
    @JsonGetter("history")
    public List<Move> getSerializedHistory() {
        return this.history;
    }

    /**
     * Retrieves the Variant object representing the type of Shogi game being played.
     *
     * @return The Variant object.
     * @throws IllegalArgumentException If the variant is not recognized.
     */
    public Variant getVariant() {
        return switch (variant) {
            case "Standard" -> new Standard();
            case "Mini" -> new Mini();
            default -> throw new IllegalArgumentException("Unsupported variant in save file: " + variant);
        };
    }

    /**
     * Retrieves the remaining time for a given player.
     *
     * @param side The side (Sente or Gote) whose time is being requested.
     * @return The remaining time for the player.
     */
    @JsonIgnore
    public int getTime(Side side) {
        return timeLeft.get(side);
    }

    /**
     * Retrieves the serialized remaining time for both players (Sente and Gote) for JSON serialization.
     *
     * @return A map containing the remaining time for each player.
     */
    @JsonGetter("timeLeft")
    public Map<Side, Integer> getSerializedTimeLeft() {
        return timeLeft;
    }
}
