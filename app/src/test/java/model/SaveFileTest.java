package model;

import com.fasterxml.jackson.core.type.TypeReference;
import model.game.Game;
import model.game.History;
import model.pieces.Pawn;
import model.variants.Mini;
import model.variants.Standard;
import model.variants.Variant;
import org.junit.jupiter.api.*;
import util.JsonLoader;
import util.PathResolver;
import util.Pos;
import util.Side;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SaveFileTest {

    @BeforeEach
    void setUp() throws IOException {
        // Ensure a clean environment for each test
        Files.deleteIfExists(PathResolver.getAppDataPath("save.json"));
    }

    @Test
    void testConstructorFromGame() {
        Game game = new Game(new Standard(), 0);
        SaveFile saveFile = new SaveFile(game);

        assertEquals(game.getSfen().toString(), saveFile.getSerializedSfen());
        assertEquals(game.getVariant().serialize(), "Standard");
        assertNotNull(saveFile.getSerializedHistory());
        assertEquals(game.getTime(Side.SENTE), saveFile.getTime(Side.SENTE));
        assertEquals(game.getTime(Side.GOTE), saveFile.getTime(Side.GOTE));
    }

    @Test
    void testSerializationAndDeserialization() throws IOException {
        SaveFile original = new SaveFile(
                "rbsgk/4p/5/P4/KGSBR b - 1",
                List.of(new Move(new Pos(1, 1), new Pos(2, 1), new Pawn(Side.GOTE), new Pawn(Side.SENTE))), // Dummy moves
                "Mini",
                Map.of(Side.SENTE, 600, Side.GOTE, 600)
        );

        Path savePath = PathResolver.getAppDataPath("save.json");
        Files.createDirectories(savePath.getParent());
        JsonLoader.getObjectMapper().writeValue(savePath.toFile(), original);

        SaveFile loaded = JsonLoader.load(savePath.toString(), new TypeReference<>() {});

        assertEquals(original.getSerializedSfen(), loaded.getSerializedSfen());
        assertEquals(original.getSerializedHistory().toString(), loaded.getSerializedHistory().toString());
        assertEquals(original.getVariant().getClass(), loaded.getVariant().getClass());
        assertEquals(original.getSerializedTimeLeft(), loaded.getSerializedTimeLeft());
    }

    @Test
    void testSaveAndLoad() {
        SaveFile saveFile = new SaveFile(
                "sfen_string",
                List.of(new Move(new Pos(1, 1), new Pos(2, 1), new Pawn(Side.GOTE), new Pawn(Side.SENTE))), // Dummy moves
                "Standard",
                Map.of(Side.SENTE, 600, Side.GOTE, 600)
        );

        saveFile.save();

        SaveFile loaded = SaveFile.load();

        assertEquals(saveFile.getSerializedSfen(), loaded.getSerializedSfen());
        assertEquals(saveFile.getSerializedHistory().toString(), loaded.getSerializedHistory().toString());
        assertEquals(saveFile.getVariant().getClass(), loaded.getVariant().getClass());
        assertEquals(saveFile.getSerializedTimeLeft(), loaded.getSerializedTimeLeft());
    }

    @Test
    void testGetVariant() {
        SaveFile saveFileStandard = new SaveFile("rbsgk/4p/5/P4/KGSBR b - 1", List.of(), "Standard", Map.of());
        SaveFile saveFileMini = new SaveFile("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1", List.of(), "Mini", Map.of());

        assertInstanceOf(Standard.class, saveFileStandard.getVariant());
        assertInstanceOf(Mini.class, saveFileMini.getVariant());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new SaveFile("sfen", List.of(), "Unknown", Map.of()).getVariant()
        );
    }

    @Test
    void testGetTime() {
        SaveFile saveFile = new SaveFile("sfen", List.of(), "Standard", Map.of(Side.SENTE, 600, Side.GOTE, 1200));

        assertEquals(600, saveFile.getTime(Side.SENTE));
        assertEquals(1200, saveFile.getTime(Side.GOTE));
    }

    @Test
    void testGetHistory() {
        Move move = new Move(new Pos(1, 1), new Pos(2, 1), new Pawn(Side.GOTE), new Pawn(Side.SENTE)); // Dummy move
        SaveFile saveFile = new SaveFile("sfen", List.of(move), "Standard", Map.of());

        History history = saveFile.getHistory();
        assertEquals(1, history.getNumberOfMoves());
        assertEquals(move, history.getMoves().next());
    }

    @AfterEach
    void tearDown() throws IOException {
        // Cleanup any created files
        Files.deleteIfExists(PathResolver.getAppDataPath("save.json"));
    }

    @Test
    void testSaveExceptionHandling() {
        // Create a SaveFile instance with dummy data
        SaveFile saveFile = new SaveFile(
                "sfen_string",
                List.of(new Move(new Pos(1, 1), new Pos(2, 1), new Pawn(Side.GOTE), new Pawn(Side.SENTE))), // Dummy moves
                "Standard",
                Map.of(Side.SENTE, 600, Side.GOTE, 600)
        );

        // Determine invalid path based on the operating system
        String invalidPath;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            // Invalid path for Windows
            invalidPath = "C:/Windows/System32/forbidden/file.json";
        } else {
            // Invalid path for Linux/Unix
            invalidPath = "/root/forbidden/file.json";
        }

        // Capture System.err output
        java.io.ByteArrayOutputStream errStream = new java.io.ByteArrayOutputStream();
        System.setErr(new java.io.PrintStream(errStream));

        // Attempt to save to an invalid path
        saveFile.save(invalidPath);

        // Restore original System.err
        System.setErr(System.err);

        // Verify the expected error message is logged
        String loggedMessage = errStream.toString();
        assertTrue(
                loggedMessage.contains("Failed to save save file"),
                "Expected error message not found. Found: " + loggedMessage
        );
    }
}

