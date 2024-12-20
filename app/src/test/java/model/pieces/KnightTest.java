package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KnightTest {

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
        Knight knight = new Knight(Side.SENTE);
        Pos knightPos = new Pos(4, 4); // Central position

        // Act
        ArrayList<Pos> moves = knight.getAvailableMoves(knightPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(2, moves.size(), "Knight should have 2 valid moves for SENTE on an empty board.");
        assertTrue(moves.contains(new Pos(2, 3)), "Knight should allow move to (2,3).");
        assertTrue(moves.contains(new Pos(2, 5)), "Knight should allow move to (2,5).");
    }

    @Test
    void testGetAvailableMoves_Gote_EmptyBoard() {
        // Arrange
        TestBoard board = new TestBoard();
        Knight knight = new Knight(Side.GOTE);
        Pos knightPos = new Pos(4, 4); // Central position

        // Act
        ArrayList<Pos> moves = knight.getAvailableMoves(knightPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(2, moves.size(), "Knight should have 2 valid moves for GOTE on an empty board.");
        assertTrue(moves.contains(new Pos(6, 5)), "Knight should allow move to (6,5).");
        assertTrue(moves.contains(new Pos(6, 3)), "Knight should allow move to (6,3).");
    }

    @Test
    void testGetAvailableMoves_Promoted_EmptyBoard() {
        // Arrange
        TestBoard board = new TestBoard();
        Knight knight = new Knight(Side.SENTE);
        knight.promote(); // Promote the Knight
        Pos knightPos = new Pos(4, 4); // Central position

        // Act
        ArrayList<Pos> moves = knight.getAvailableMoves(knightPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(6, moves.size(), "Promoted Knight should have 6 valid moves.");
        assertTrue(moves.contains(new Pos(3, 3)), "Promoted Knight should allow move to (3,3).");
        assertTrue(moves.contains(new Pos(5, 4)), "Promoted Knight should allow move to (5,4).");
    }

    @Test
    void testGetAvailableMoves_BlockedByFriendlyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        Knight knight = new Knight(Side.SENTE);
        Pawn friendlyPawn = new Pawn(Side.SENTE);
        Pos knightPos = new Pos(4, 4);
        Pos blockingPos = new Pos(2, 3);

        board.placePiece(knight, knightPos);
        board.placePiece(friendlyPawn, blockingPos); // Friendly pawn blocks move (2,3)

        // Act
        ArrayList<Pos> moves = knight.getAvailableMoves(knightPos, board);

        // Assert
        assertNotNull(moves);
        assertFalse(moves.contains(blockingPos), "Knight shouldn't move to a square occupied by a friendly piece.");
        assertEquals(1, moves.size(), "Knight should have 1 valid move left.");
    }

    @Test
    void testGetAvailableMoves_BlockedByEnemyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        Knight knight = new Knight(Side.SENTE);
        Pawn enemyPawn = new Pawn(Side.GOTE);
        Pos knightPos = new Pos(4, 4);
        Pos blockingPos = new Pos(2, 3);

        board.placePiece(knight, knightPos);
        board.placePiece(enemyPawn, blockingPos); // Enemy pawn blocks move (2,3)

        // Act
        ArrayList<Pos> moves = knight.getAvailableMoves(knightPos, board);

        // Assert
        assertNotNull(moves);
        assertTrue(moves.contains(blockingPos), "Knight should capture an enemy piece.");
        assertEquals(2, moves.size(), "Knight should have 2 valid moves including capture.");
    }

    @Test
    void testGetAvailableMoves_EdgeOfBoard() {
        // Arrange
        TestBoard board = new TestBoard();
        Knight knight = new Knight(Side.SENTE);
        Pos knightPos = new Pos(0, 4); // Near top edge of the board

        // Act
        ArrayList<Pos> moves = knight.getAvailableMoves(knightPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(0, moves.size(), "Knight should have no valid moves near the top edge for SENTE.");
    }

    @Test
    void testGetAvailableMoves_HiddenInCorner() {
        // Arrange
        TestBoard board = new TestBoard();
        Knight knight = new Knight(Side.SENTE);
        Pos knightPos = new Pos(0, 0); // Top-left corner

        // Act
        ArrayList<Pos> moves = knight.getAvailableMoves(knightPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(0, moves.size(), "At the top-left corner, Knight should have no valid moves for SENTE.");
    }

    @Test
    void testGetAvailableMovesBackend_SameAsFrontend() {
        // Arrange
        TestBoard board = new TestBoard();
        Knight knight = new Knight(Side.SENTE);
        Pos knightPos = new Pos(4, 4);

        // Act
        ArrayList<Pos> frontendMoves = knight.getAvailableMoves(knightPos, board);
        ArrayList<Pos> backendMoves = knight.getAvailableMovesBackend(knightPos, board);

        // Assert
        assertNotNull(backendMoves);
        assertEquals(frontendMoves, backendMoves, "Backend moves should match frontend moves.");
    }

    @Test
    void testGetSfenAbbreviation_Unpromoted() {
        // Arrange
        Knight knight = new Knight(Side.SENTE);

        // Act
        String abbreviation = knight.getSfenAbbreviation();

        // Assert
        assertEquals("N", abbreviation, "Unpromoted Knight should have SFEN abbreviation 'N'.");
    }

    @Test
    void testGetSfenAbbreviation_Promoted() {
        // Arrange
        Knight knight = new Knight(Side.SENTE);
        knight.promote();

        // Act
        String abbreviation = knight.getSfenAbbreviation();

        // Assert
        assertEquals("+N", abbreviation, "Promoted Knight should have SFEN abbreviation '+N'.");
    }
}