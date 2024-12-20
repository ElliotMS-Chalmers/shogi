package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LanceTest {

    private static class TestBoard extends Board {
        private final Piece[][] board;

        public TestBoard() {
            super(8, 8); // Standard 8x8 board
            this.board = new Piece[8][8];
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
            if (isWithinBounds(pos)) {
                board[pos.row()][pos.col()] = piece;
            }
        }

        public void removePiece(Pos pos) {
            if (isWithinBounds(pos)) {
                board[pos.row()][pos.col()] = null;
            }
        }
    }

    @Test
    void testGetSfenAbbreviation_Unpromoted() {
        Lance lance = new Lance(Side.SENTE);
        assertEquals("L", lance.getSfenAbbreviation(), "Unpromoted Lance should have SFEN abbreviation 'L'.");
    }

    @Test
    void testGetSfenAbbreviation_Promoted() {
        Lance lance = new Lance(Side.SENTE);
        lance.promote();
        assertEquals("+L", lance.getSfenAbbreviation(), "Promoted Lance should have SFEN abbreviation '+L'.");
    }

    @Test
    void testGetAvailableMoves_Unpromoted_NoObstacles() {
        TestBoard board = new TestBoard();
        Lance lance = new Lance(Side.SENTE);
        Pos lancePos = new Pos(4, 4);
        board.placePiece(lance, lancePos);

        ArrayList<Pos> moves = lance.getAvailableMoves(lancePos, board);

        assertNotNull(moves);
        assertEquals(4, moves.size(), "Lance should have 3 moves in a straight line.");
        assertTrue(moves.contains(new Pos(3, 4)));
        assertTrue(moves.contains(new Pos(2, 4)));
        assertTrue(moves.contains(new Pos(1, 4)));
        assertTrue(moves.contains(new Pos(0, 4)));
    }

    @Test
    void testGetAvailableMoves_Unpromoted_FriendlyPieceBlocking() {
        TestBoard board = new TestBoard();
        Lance lance = new Lance(Side.SENTE);
        Pos lancePos = new Pos(4, 4);
        Pos blockingPos = new Pos(2, 4);
        board.placePiece(lance, lancePos);
        board.placePiece(new Lance(Side.SENTE), blockingPos); // Friendly piece blocks path

        ArrayList<Pos> moves = lance.getAvailableMoves(lancePos, board);

        assertNotNull(moves);
        assertEquals(1, moves.size(), "Lance should only move up to the position before the friendly piece.");
        assertTrue(moves.contains(new Pos(3, 4)));
        assertFalse(moves.contains(new Pos(2, 4)));
    }

    @Test
    void testGetAvailableMoves_Unpromoted_EnemyPieceBlocking() {
        TestBoard board = new TestBoard();
        Lance lance = new Lance(Side.SENTE);
        Pos lancePos = new Pos(4, 4);
        Pos blockingPos = new Pos(2, 4);
        board.placePiece(lance, lancePos);
        board.placePiece(new Lance(Side.GOTE), blockingPos); // Enemy piece blocks path

        ArrayList<Pos> moves = lance.getAvailableMoves(lancePos, board);

        assertNotNull(moves);
        assertEquals(2, moves.size(), "Lance should move up to and include the enemy piece's position.");
        assertTrue(moves.contains(new Pos(3, 4)));
        assertTrue(moves.contains(new Pos(2, 4))); // Captures enemy piece
    }

    @Test
    void testGetAvailableMoves_Unpromoted_EdgeOfBoard() {
        TestBoard board = new TestBoard();
        Lance lance = new Lance(Side.SENTE);
        Pos lancePos = new Pos(0, 4); // Top edge of the board
        board.placePiece(lance, lancePos);

        ArrayList<Pos> moves = lance.getAvailableMoves(lancePos, board);

        assertNotNull(moves);
        assertEquals(0, moves.size(), "Lance at the top edge should have no valid moves.");
    }

    @Test
    void testGetAvailableMoves_Promoted_NoObstacles() {
        TestBoard board = new TestBoard();
        Lance lance = new Lance(Side.SENTE);
        lance.promote();
        Pos lancePos = new Pos(4, 4); // Central position
        board.placePiece(lance, lancePos);

        ArrayList<Pos> moves = lance.getAvailableMoves(lancePos, board);

        assertNotNull(moves);
        assertEquals(6, moves.size(), "Promoted Lance should have 6 moves in all directions.");
        assertTrue(moves.contains(new Pos(3, 3)));
        assertTrue(moves.contains(new Pos(3, 4)));
        assertTrue(moves.contains(new Pos(3, 5)));
        assertTrue(moves.contains(new Pos(4, 3)));
        assertTrue(moves.contains(new Pos(4, 5)));
        assertTrue(moves.contains(new Pos(5, 4)));
    }

    @Test
    void testGetAvailableMoves_Promoted_BlockedByFriendlyPiece() {
        TestBoard board = new TestBoard();
        Lance lance = new Lance(Side.SENTE);
        lance.promote();
        Pos lancePos = new Pos(4, 4);
        Pos blockingPos = new Pos(3, 3);
        board.placePiece(lance, lancePos);
        board.placePiece(new Lance(Side.SENTE), blockingPos); // Friendly piece blocks one diagonal direction

        ArrayList<Pos> moves = lance.getAvailableMoves(lancePos, board);

        assertNotNull(moves);
        assertEquals(5, moves.size(), "Promoted Lance should have one position blocked by the friendly piece.");
        assertFalse(moves.contains(blockingPos));
    }

    @Test
    void testPromote() {
        Lance lance = new Lance(Side.SENTE);

        assertFalse(lance.getIsPromoted(), "Lance should start unpromoted.");
        lance.promote();
        assertTrue(lance.getIsPromoted(), "Lance should be promoted after calling promote().");
    }

    @Test
    void testGetAvailableMovesBackend_SameAsFrontend() {
        TestBoard board = new TestBoard();
        Lance lance = new Lance(Side.SENTE);
        Pos lancePos = new Pos(4, 4);
        board.placePiece(lance, lancePos);

        ArrayList<Pos> frontendMoves = lance.getAvailableMoves(lancePos, board);
        ArrayList<Pos> backendMoves = lance.getAvailableMovesBackend(lancePos, board);

        assertEquals(frontendMoves, backendMoves, "Frontend and Backend moves should match.");
    }

    @Test
    void testGetAvailableMovesBackend_Promoted_NoObstacles() {
        TestBoard board = new TestBoard();
        Lance lance = new Lance(Side.SENTE);
        lance.promote();
        Pos lancePos = new Pos(4, 4); // Central position
        board.placePiece(lance, lancePos);

        ArrayList<Pos> moves = lance.getAvailableMovesBackend(lancePos, board);

        assertNotNull(moves);
        assertEquals(6, moves.size(), "Promoted Lance should have 6 moves in all directions.");
        assertTrue(moves.contains(new Pos(3, 3)));
        assertTrue(moves.contains(new Pos(3, 4)));
        assertTrue(moves.contains(new Pos(3, 5)));
        assertTrue(moves.contains(new Pos(4, 3)));
        assertTrue(moves.contains(new Pos(4, 5)));
        assertTrue(moves.contains(new Pos(5, 4)));
    }

    @Test
    void testGetAvailableMovesBackend_BlockByEnemyKing() {
        // Arrange
        TestBoard board = new TestBoard();
        Lance lance = new Lance(Side.SENTE);
        King enemyKing = new King(Side.GOTE);
        Pos lancePos = new Pos(4, 4);
        Pos kingPos = new Pos(3, 4);

        board.placePiece(lance, lancePos);
        board.placePiece(enemyKing, kingPos);

        // Act
        ArrayList<Pos> moves = lance.getAvailableMovesBackend(lancePos, board);

        // Assert
        assertNotNull(moves);
        assertTrue(moves.contains(new Pos(3, 4)), "Lance can capture the enemy king in backend logic.");
        assertTrue(moves.contains(new Pos(2, 4)), "Lance blocks behind the enemy king in backend logic.");
    }
}