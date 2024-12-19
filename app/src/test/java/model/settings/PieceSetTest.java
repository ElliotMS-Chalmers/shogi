package model.settings;

import model.pieces.Pawn;
import model.pieces.Piece;
import util.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class PieceSetTest {

    private PieceSet pieceSet;

    @BeforeEach
    void setUp() {
        pieceSet = new PieceSet("Ryoko_1Kanji");
    }

    @Test
    void constructorShouldInitializeFields() {
        assertEquals("Ryoko_1Kanji", pieceSet.getDirectory());
    }

    @Test
    void getImageShouldReturnInputStreamForValidPiece() {
        // Create a mock Piece instance (manually, no mocking libraries used)
        Piece validPiece = new Pawn(Side.SENTE);

        InputStream inputStream = pieceSet.getImage(validPiece);
        assertNotNull(inputStream, "InputStream should not be null for a valid piece image.");

        // Clean up InputStream
        try {
            inputStream.close();
        } catch (Exception e) {
            fail("Failed to close InputStream: " + e.getMessage());
        }
    }

    @Test
    void getImageShouldCacheValidPieceImages() {
        // Create a valid Piece
        Piece validPiece = new Pawn(Side.SENTE);

        // Load image for the first time
        InputStream firstLoad = pieceSet.getImage(validPiece);

        // Close the first InputStream
        try {
            firstLoad.close();
        } catch (Exception e) {
            fail("Failed to close InputStream: " + e.getMessage());
        }

        // Load image for the second time
        InputStream secondLoad = pieceSet.getImage(validPiece);
        assertNotNull(secondLoad, "InputStream should not be null on subsequent load.");
        assertInstanceOf(ByteArrayInputStream.class, secondLoad, "Subsequent load should return a cached InputStream.");

        // Close the second InputStream
        try {
            secondLoad.close();
        } catch (Exception e) {
            fail("Failed to close InputStream: " + e.getMessage());
        }
    }

    @Test
    void getImageShouldReturnNullForNullPiece() {
        InputStream inputStream = pieceSet.getImage(null);
        assertNull(inputStream, "InputStream should be null for a null piece.");
    }

    @Test
    void getImageShouldThrowRuntimeExceptionWhenImageNotFound() {
        PieceSet pieceSet = new PieceSet("nonexistentdirectory");
        Piece piece = new Pawn(Side.SENTE);

        assertThrows(RuntimeException.class, () -> pieceSet.getImage(piece));
    }
}
