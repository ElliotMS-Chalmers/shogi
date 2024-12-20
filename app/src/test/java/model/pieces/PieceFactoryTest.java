package model.pieces;

import org.junit.jupiter.api.Test;
import util.Side;

import static org.junit.jupiter.api.Assertions.*;

class PieceFactoryTest {

    // --- Tests for fromSfenAbbreviation ---

    @Test
    void testFromSfenAbbreviation_KingSente() {
        // Act
        Piece piece = PieceFactory.fromSfenAbbreviation("K");

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof King, "Expected King instance.");
        assertEquals(Side.SENTE, piece.getSide(), "Expected side SENTE.");
    }

    @Test
    void testFromSfenAbbreviation_KingGote() {
        // Act
        Piece piece = PieceFactory.fromSfenAbbreviation("k");

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof King, "Expected King instance.");
        assertEquals(Side.GOTE, piece.getSide(), "Expected side GOTE.");
    }

    @Test
    void testFromSfenAbbreviation_RookSente() {
        // Act
        Piece piece = PieceFactory.fromSfenAbbreviation("R");

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof Rook, "Expected Rook instance.");
        assertEquals(Side.SENTE, piece.getSide(), "Expected side SENTE.");
    }

    @Test
    void testFromSfenAbbreviation_BishopGote() {
        // Act
        Piece piece = PieceFactory.fromSfenAbbreviation("b");

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof Bishop, "Expected Bishop instance.");
        assertEquals(Side.GOTE, piece.getSide(), "Expected side GOTE.");
    }

    @Test
    void testFromSfenAbbreviation_GoldGeneralSente() {
        // Act
        Piece piece = PieceFactory.fromSfenAbbreviation("G");

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof GoldGeneral, "Expected GoldGeneral instance.");
        assertEquals(Side.SENTE, piece.getSide(), "Expected side SENTE.");
    }

    @Test
    void testFromSfenAbbreviation_SilverGeneralGote() {
        // Act
        Piece piece = PieceFactory.fromSfenAbbreviation("s");

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof SilverGeneral, "Expected SilverGeneral instance.");
        assertEquals(Side.GOTE, piece.getSide(), "Expected side GOTE.");
    }

    @Test
    void testFromSfenAbbreviation_KnightSente() {
        // Act
        Piece piece = PieceFactory.fromSfenAbbreviation("N");

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof Knight, "Expected Knight instance.");
        assertEquals(Side.SENTE, piece.getSide(), "Expected side SENTE.");
    }

    @Test
    void testFromSfenAbbreviation_LanceGote() {
        // Act
        Piece piece = PieceFactory.fromSfenAbbreviation("l");

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof Lance, "Expected Lance instance.");
        assertEquals(Side.GOTE, piece.getSide(), "Expected side GOTE.");
    }

    @Test
    void testFromSfenAbbreviation_PawnSente() {
        // Act
        Piece piece = PieceFactory.fromSfenAbbreviation("P");

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof Pawn, "Expected Pawn instance.");
        assertEquals(Side.SENTE, piece.getSide(), "Expected side SENTE.");
    }

    @Test
    void testFromSfenAbbreviation_PromotedPawnGote() {
        // Act
        Piece piece = PieceFactory.fromSfenAbbreviation("+p");

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof Pawn, "Expected Pawn instance.");
        assertTrue(piece instanceof Promotable, "Expected Promotable instance.");
        assertTrue(((Promotable) piece).getIsPromoted(), "Expected piece to be promoted.");
        assertEquals(Side.GOTE, piece.getSide(), "Expected side GOTE.");
    }

    @Test
    void testFromSfenAbbreviation_InvalidType() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                PieceFactory.fromSfenAbbreviation("X"));

        assertEquals("Invalid sfen abbreviation: X", exception.getMessage(),
                "Expected exception message to match.");
    }

    @Test
    void testFromSfenAbbreviation_InvalidPromotionLetter() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                PieceFactory.fromSfenAbbreviation("+x"));

        assertEquals("Invalid sfen abbreviation: +x", exception.getMessage(),
                "Expected exception message to match.");
    }

    // --- Tests for fromClass ---

    @Test
    void testFromClass_PawnSente() {
        // Act
        Piece piece = PieceFactory.fromClass(Pawn.class, Side.SENTE);

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof Pawn, "Expected Pawn instance.");
        assertEquals(Side.SENTE, piece.getSide(), "Expected side SENTE.");
    }

    @Test
    void testFromClass_RookGote() {
        // Act
        Piece piece = PieceFactory.fromClass(Rook.class, Side.GOTE);

        // Assert
        assertNotNull(piece);
        assertTrue(piece instanceof Rook, "Expected Rook instance.");
        assertEquals(Side.GOTE, piece.getSide(), "Expected side GOTE.");
    }


}