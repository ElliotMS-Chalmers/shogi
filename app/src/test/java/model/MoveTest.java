package model;

import model.pieces.Pawn;
import model.pieces.Piece;
import util.Pos;
import util.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {


    @Test
    void testMoveConstructor_withValidParameters() {
        // Arrange
        Pos from = new Pos(2, 3);
        Pos to = new Pos(3, 4);
        Piece movedPiece = new Pawn(Side.SENTE);
        Piece capturedPiece = new Pawn(Side.GOTE);

        // Act
        Move move = new Move(from, to, movedPiece, capturedPiece);

        // Assert
        assertEquals(from, move.from());
        assertEquals(to, move.to());
        assertEquals(movedPiece, move.movedPiece());
        assertEquals(capturedPiece, move.capturedPiece());
    }

    @Test
    void testFromPlayerHand_withNullFrom() {
        // Arrange
        Pos to = new Pos(3, 4);
        Piece movedPiece = new Pawn(Side.SENTE);

        // Act
        Move move = new Move(null, to, movedPiece, null);

        // Assert
        assertTrue(move.fromPlayerHand());
    }

    @Test
    void testFromPlayerHand_withNonNullFrom() {
        // Arrange
        Pos from = new Pos(2, 3);
        Pos to = new Pos(3, 4);
        Piece movedPiece = new Pawn(Side.SENTE);

        // Act
        Move move = new Move(from, to, movedPiece, null);

        // Assert
        assertFalse(move.fromPlayerHand());
    }

    @Test
    void testToString_withCapture() {
        // Arrange
        Pos from = new Pos(1, 2);
        Pos to = new Pos(2, 3);
        Piece movedPiece = new Pawn(Side.SENTE);
        Piece capturedPiece = new Pawn(Side.GOTE);

        // Act
        Move move = new Move(from, to, movedPiece, capturedPiece);

        // Assert
        assertEquals("Px43", move.toString());
    }

    @Test
    void testToString_withNormalMove() {
        // Arrange
        Pos from = new Pos(1, 2);
        Pos to = new Pos(2, 3);
        Piece movedPiece = new Pawn(Side.SENTE);

        // Act
        Move move = new Move(from, to, movedPiece, null);

        // Assert
        assertEquals("P-43", move.toString());
    }

    @Test
    void testToString_fromPlayerHand() {
        // Arrange
        Pos to = new Pos(2, 3);
        Piece movedPiece = new Pawn(Side.SENTE);

        // Act
        Move move = new Move(null, to, movedPiece, null);

        // Assert
        assertEquals("P*43", move.toString());
    }
}