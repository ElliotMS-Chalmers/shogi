package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CopperGeneralTest {

    private static class TestBoard extends Board {
        private final Piece[][] board;

        public TestBoard() {
            super(8, 8); // Standard 8x8 bräde
            board = new Piece[8][8];
        }

        @Override
        public Piece getPieceAt(Pos pos) {
            if (isWithinBounds(pos)) {
                return board[pos.row()][pos.col()];
            }
            return null;
        }


        public boolean isWithinBounds(Pos pos) {
            return pos.row() >= 0 && pos.row() < 8 && pos.col() >= 0 && pos.col() < 8;
        }

        public void placePiece(Piece piece, Pos pos) {
            board[pos.row()][pos.col()] = piece;
        }

        public void removePiece(Pos pos) {
            board[pos.row()][pos.col()] = null;
        }
    }

    @Test
    void testGetAvailableMoves_EmptyBoard() {
        // Arrange
        TestBoard board = new TestBoard();
        CopperGeneral piece = new CopperGeneral(Side.SENTE);
        Pos piecePos = new Pos(4, 4); // Central position

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(piecePos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(4, moves.size(), "CopperGeneral should have 4 valid non-promoted moves.");
        assertTrue(moves.contains(new Pos(3, 3)), "Move to top-left diagonal should be valid.");
        assertTrue(moves.contains(new Pos(3, 5)), "Move to top-right diagonal should be valid.");
    }

    @Test
    void testGetAvailableMoves_BlockedByFriendlyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        CopperGeneral piece = new CopperGeneral(Side.SENTE);
        Pawn friendlyPawn = new Pawn(Side.SENTE);
        Pos piecePos = new Pos(4, 4);
        Pos blockingPos = new Pos(3, 3);

        board.placePiece(piece, piecePos);
        board.placePiece(friendlyPawn, blockingPos); // Friendly pawn blocks top-left diagonal move

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(piecePos, board);

        // Assert
        assertNotNull(moves);
        assertFalse(moves.contains(new Pos(3, 3)), "CopperGeneral shouldn't move to a square occupied by a friendly piece.");
        assertEquals(3, moves.size(), "Blocked by a friendly piece, should have 3 valid moves left.");
    }

    @Test
    void testGetAvailableMoves_BlockedByEnemyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        CopperGeneral piece = new CopperGeneral(Side.SENTE);
        Pawn enemyPawn = new Pawn(Side.GOTE);
        Pos piecePos = new Pos(4, 4);
        Pos blockingPos = new Pos(3, 3);

        board.placePiece(piece, piecePos);
        board.placePiece(enemyPawn, blockingPos); // Enemy pawn blocks top-left diagonal

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(piecePos, board);

        // Assert
        assertNotNull(moves);
        assertTrue(moves.contains(new Pos(3, 3)), "CopperGeneral should capture an enemy piece.");
        assertEquals(4, moves.size(), "CopperGeneral with an enemy piece blocking should still have 4 valid moves including capture.");
    }

    @Test
    void testGetAvailableMoves_Promoted_EmptyBoard() {
        // Arrange
        TestBoard board = new TestBoard();
        CopperGeneral piece = new CopperGeneral(Side.GOTE);
        piece.promote(); // Promote the Copper General
        Pos piecePos = new Pos(4, 4);

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(piecePos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(0, moves.size(), "Promoted CopperGeneral should have no valid moves (no defined promoted moves).");
    }

    @Test
    void testGetAvailableMovesBackend_SameAsFrontend() {
        // Arrange
        TestBoard board = new TestBoard();
        CopperGeneral piece = new CopperGeneral(Side.SENTE);
        Pos piecePos = new Pos(4, 4);

        // Act
        ArrayList<Pos> frontendMoves = piece.getAvailableMoves(piecePos, board);
        ArrayList<Pos> backendMoves = piece.getAvailableMovesBackend(piecePos, board);

        // Assert
        assertNotNull(backendMoves);
        assertEquals(frontendMoves, backendMoves, "Backend moves should match frontend moves.");
    }

    @Test
    void testGetAvailableMoves_InvalidPosition() {
        // Arrange
        TestBoard board = new TestBoard();
        CopperGeneral piece = new CopperGeneral(Side.SENTE);
        Pos invalidPos = new Pos(10, 10); // Outside the board

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(invalidPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(0, moves.size(), "CopperGeneral should have no moves for positions outside the board.");
    }

    @Test
    void testGetAvailableMoves_EdgeOfBoard() {
        // Arrange
        TestBoard board = new TestBoard();
        CopperGeneral piece = new CopperGeneral(Side.SENTE);
        Pos piecePos = new Pos(0, 0); // Top-left corner

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(piecePos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(1, moves.size(), "CopperGeneral at the edge of the board should only have 1 valid moves.");
        assertFalse(moves.contains(new Pos(1, 1)), "Move to (1,1) should not be valid.");
    }
}