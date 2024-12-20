package model.pieces;

import model.Board;
import util.Pos;
import util.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GoldGeneralTest {

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
        GoldGeneral piece = new GoldGeneral(Side.SENTE);
        Pos piecePos = new Pos(4, 4); // Central position

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(piecePos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(6, moves.size(), "GoldGeneral should have 6 valid moves on an empty board.");
        assertTrue(moves.contains(new Pos(3, 4)), "GoldGeneral should allow forward move.");
        assertTrue(moves.contains(new Pos(3, 3)), "GoldGeneral should allow diagonal top-left move.");
        assertTrue(moves.contains(new Pos(4, 3)), "GoldGeneral should allow left move.");
    }

    @Test
    void testGetAvailableMoves_BlockedByFriendlyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        GoldGeneral piece = new GoldGeneral(Side.SENTE);
        Pawn friendlyPawn = new Pawn(Side.SENTE);
        Pos piecePos = new Pos(4, 4);
        Pos blockingPos = new Pos(3, 4);

        board.placePiece(piece, piecePos);
        board.placePiece(friendlyPawn, blockingPos); // Friendly pawn blocks forward move

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(piecePos, board);

        // Assert
        assertNotNull(moves);
        assertFalse(moves.contains(new Pos(3, 4)), "GoldGeneral shouldn't move to a square occupied by a friendly piece.");
        assertEquals(5, moves.size(), "Blocked by a friendly piece, GoldGeneral should have 5 valid moves left.");
    }

    @Test
    void testGetAvailableMoves_BlockedByEnemyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        GoldGeneral piece = new GoldGeneral(Side.SENTE);
        Pawn enemyPawn = new Pawn(Side.GOTE);
        Pos piecePos = new Pos(4, 4);
        Pos blockingPos = new Pos(3, 4);

        board.placePiece(piece, piecePos);
        board.placePiece(enemyPawn, blockingPos); // Enemy pawn blocks forward move

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(piecePos, board);

        // Assert
        assertNotNull(moves);
        assertTrue(moves.contains(new Pos(3, 4)), "GoldGeneral should capture an enemy piece.");
        assertEquals(6, moves.size(), "GoldGeneral with an enemy blocking should still have 6 valid moves including capture.");
    }

    @Test
    void testGetAvailableMoves_TopEdge() {
        // Arrange
        TestBoard board = new TestBoard();
        GoldGeneral piece = new GoldGeneral(Side.SENTE);
        Pos piecePos = new Pos(0, 4); // Top edge position

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(piecePos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(3, moves.size(), "At the top edge, GoldGeneral should have 3 valid moves.");
        assertTrue(moves.contains(new Pos(1, 4)), "Should include backward move.");
        assertTrue(moves.contains(new Pos(0, 3)), "Should include left move.");
    }

    @Test
    void testGetAvailableMoves_EdgeCase() {
        // Arrange
        TestBoard board = new TestBoard();
        GoldGeneral piece = new GoldGeneral(Side.SENTE);
        Pos piecePos = new Pos(0, 0); // Top-left corner

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(piecePos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(2, moves.size(), "At the top-left corner, GoldGeneral should have 2 valid moves.");
        assertTrue(moves.contains(new Pos(0, 1)), "Should include move to the right.");
        assertTrue(moves.contains(new Pos(1, 0)), "Should include move downward.");
    }

    @Test
    void testGetAvailableMoves_InvalidPosition() {
        // Arrange
        TestBoard board = new TestBoard();
        GoldGeneral piece = new GoldGeneral(Side.SENTE);
        Pos invalidPos = new Pos(10, 10); // Outside the board

        // Act
        ArrayList<Pos> moves = piece.getAvailableMoves(invalidPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(0, moves.size(), "GoldGeneral should have no moves for positions outside the board.");
    }

    @Test
    void testGetAvailableMovesBackend_SameAsFrontend() {
        // Arrange
        TestBoard board = new TestBoard();
        GoldGeneral piece = new GoldGeneral(Side.SENTE);
        Pos piecePos = new Pos(4, 4);

        // Act
        ArrayList<Pos> frontendMoves = piece.getAvailableMoves(piecePos, board);
        ArrayList<Pos> backendMoves = piece.getAvailableMovesBackend(piecePos, board);

        // Assert
        assertNotNull(backendMoves);
        assertEquals(frontendMoves, backendMoves, "Backend moves should match frontend moves.");
    }
}