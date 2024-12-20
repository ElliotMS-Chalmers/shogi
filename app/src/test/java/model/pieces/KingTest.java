package model.pieces;

import model.Board;
import util.Pos;
import util.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

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
        King king = new King(Side.SENTE);
        Pos kingPos = new Pos(4, 4); // Central position

        // Act
        ArrayList<Pos> moves = king.getAvailableMoves(kingPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(8, moves.size(), "King should have 8 valid moves on an empty board.");
        assertTrue(moves.contains(new Pos(3, 3)), "King should allow move to top-left diagonal.");
        assertTrue(moves.contains(new Pos(5, 4)), "King should allow move downward.");
    }

    @Test
    void testGetAvailableMoves_BlockedByFriendlyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        King king = new King(Side.SENTE);
        Pawn friendlyPawn = new Pawn(Side.SENTE);
        Pos kingPos = new Pos(4, 4);
        Pos blockingPos = new Pos(3, 4);

        board.placePiece(king, kingPos);
        board.placePiece(friendlyPawn, blockingPos); // Friendly pawn blocks forward move

        // Act
        ArrayList<Pos> moves = king.getAvailableMoves(kingPos, board);

        // Assert
        assertNotNull(moves);
        assertFalse(moves.contains(new Pos(3, 4)), "King shouldn't move to a square occupied by a friendly piece.");
        assertEquals(7, moves.size(), "Blocked by a friendly piece, King should have 7 valid moves left.");
    }

    @Test
    void testGetAvailableMoves_BlockedByEnemyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        King king = new King(Side.SENTE);
        Pawn enemyPawn = new Pawn(Side.GOTE);
        Pos kingPos = new Pos(4, 4);
        Pos blockingPos = new Pos(3, 4);

        board.placePiece(king, kingPos);
        board.placePiece(enemyPawn, blockingPos); // Enemy pawn blocks forward move

        // Act
        ArrayList<Pos> moves = king.getAvailableMoves(kingPos, board);

        // Assert
        assertNotNull(moves);
        assertTrue(moves.contains(new Pos(3, 4)), "King should capture an enemy piece.");
        assertEquals(8, moves.size(), "King with an enemy blocking should still have 8 valid moves including capture.");
    }

    @Test
    void testGetAvailableMoves_TopEdge() {
        // Arrange
        TestBoard board = new TestBoard();
        King king = new King(Side.SENTE);
        Pos kingPos = new Pos(0, 4); // Top edge position

        // Act
        ArrayList<Pos> moves = king.getAvailableMoves(kingPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(5, moves.size(), "At the top edge, King should have 5 valid moves.");
        assertTrue(moves.contains(new Pos(1, 3)), "King should allow move diagonally downward.");
        assertTrue(moves.contains(new Pos(1, 4)), "King should allow move straightforward downward.");
    }

    @Test
    void testGetAvailableMoves_HiddenInCorner() {
        // Arrange
        TestBoard board = new TestBoard();
        King king = new King(Side.SENTE);
        Pos kingPos = new Pos(0, 0); // Top-left corner

        // Act
        ArrayList<Pos> moves = king.getAvailableMoves(kingPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(3, moves.size(), "At the top-left corner, King should have 3 valid moves.");
        assertTrue(moves.contains(new Pos(0, 1)), "King should include move to the right.");
        assertTrue(moves.contains(new Pos(1, 0)), "King should include downward move.");
    }

    @Test
    void testGetAvailableMoves_InvalidPosition() {
        // Arrange
        TestBoard board = new TestBoard();
        King king = new King(Side.SENTE);
        Pos invalidPos = new Pos(10, 10); // Outside the board

        // Act
        ArrayList<Pos> moves = king.getAvailableMoves(invalidPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(0, moves.size(), "King should have no moves for positions outside the board.");
    }

    @Test
    void testGetAvailableMovesBackend_SameAsFrontend() {
        // Arrange
        TestBoard board = new TestBoard();
        King king = new King(Side.SENTE);
        Pos kingPos = new Pos(4, 4);

        // Act
        ArrayList<Pos> frontendMoves = king.getAvailableMoves(kingPos, board);
        ArrayList<Pos> backendMoves = king.getAvailableMovesBackend(kingPos, board);

        // Assert
        assertNotNull(backendMoves);
        assertEquals(frontendMoves, backendMoves, "Backend moves should match frontend moves.");
    }


}