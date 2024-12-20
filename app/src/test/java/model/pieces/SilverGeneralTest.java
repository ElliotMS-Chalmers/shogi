package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SilverGeneralTest {

    private static class TestBoard extends Board {
        private final Piece[][] board;

        public TestBoard() {
            super(8, 8); // Standard 8x8 board size
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
    void testGetAvailableMoves_Unpromoted_NoObstacles() {
        // Arrange
        TestBoard board = new TestBoard();
        SilverGeneral silverGeneral = new SilverGeneral(Side.SENTE);
        Pos silverPos = new Pos(4, 4); // Place in the middle of the board
        board.placePiece(silverGeneral, silverPos);

        // Act
        ArrayList<Pos> moves = silverGeneral.getAvailableMoves(silverPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(5, moves.size(), "Unpromoted SilverGeneral should have 5 valid moves in the forward and diagonal directions.");
        assertTrue(moves.contains(new Pos(3, 3)), "SilverGeneral can move to (3, 3).");
        assertTrue(moves.contains(new Pos(3, 5)), "SilverGeneral can move to (3, 5).");
        assertTrue(moves.contains(new Pos(3, 4)), "SilverGeneral can move forward to (3, 4).");
        assertTrue(moves.contains(new Pos(5, 3)), "SilverGeneral can move to (5, 3).");
        assertTrue(moves.contains(new Pos(5, 5)), "SilverGeneral can move to (5, 5).");
    }

    @Test
    void testGetAvailableMoves_Unpromoted_BlockedByFriendlyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        SilverGeneral silverGeneral = new SilverGeneral(Side.SENTE);
        Pawn friendlyPawn = new Pawn(Side.SENTE); // Friendly piece
        Pos silverPos = new Pos(4, 4); // Place in the middle of the board
        Pos blockedPos = new Pos(3, 4); // Position directly in front of the SilverGeneral
        board.placePiece(silverGeneral, silverPos);
        board.placePiece(friendlyPawn, blockedPos);

        // Act
        ArrayList<Pos> moves = silverGeneral.getAvailableMoves(silverPos, board);

        // Assert
        assertNotNull(moves);
        assertFalse(moves.contains(blockedPos), "SilverGeneral cannot move to a position occupied by a friendly piece.");
        assertEquals(4, moves.size(), "SilverGeneral should have 4 valid moves when one is blocked by a friendly piece.");
    }

    @Test
    void testGetAvailableMoves_Unpromoted_CanCaptureEnemyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        SilverGeneral silverGeneral = new SilverGeneral(Side.SENTE);
        Pawn enemyPawn = new Pawn(Side.GOTE); // Enemy piece
        Pos silverPos = new Pos(4, 4); // Place in the middle of the board
        Pos enemyPos = new Pos(3, 4); // Position directly in front of the SilverGeneral
        board.placePiece(silverGeneral, silverPos);
        board.placePiece(enemyPawn, enemyPos);

        // Act
        ArrayList<Pos> moves = silverGeneral.getAvailableMoves(silverPos, board);

        // Assert
        assertNotNull(moves);
        assertTrue(moves.contains(enemyPos), "SilverGeneral should be able to capture an enemy piece directly in front.");
        assertEquals(5, moves.size(), "SilverGeneral should correctly calculate capturing availability.");
    }

    @Test
    void testGetAvailableMovesBackend_BlockByEnemyKing() {
        // Arrange
        TestBoard board = new TestBoard();
        SilverGeneral silverGeneral = new SilverGeneral(Side.SENTE);
        King enemyKing = new King(Side.GOTE);
        Pos silverPos = new Pos(4, 4); // Starting position of SilverGeneral
        Pos kingPos = new Pos(3, 4); // Enemy King directly in one of the movable positions
        board.placePiece(silverGeneral, silverPos);
        board.placePiece(enemyKing, kingPos);

        // Act
        ArrayList<Pos> moves = silverGeneral.getAvailableMovesBackend(silverPos, board);

        // Assert
        assertNotNull(moves);
        assertTrue(moves.contains(kingPos), "SilverGeneral should be able to move to the enemy King's position.");
        assertEquals(5, moves.size(), "Backend moves should correctly include capturing the enemy King if blocking the path.");
    }

    @Test
    void testGetAvailableMoves_Promoted_NoObstacles() {
        // Arrange
        TestBoard board = new TestBoard();
        SilverGeneral silverGeneral = new SilverGeneral(Side.SENTE);
        silverGeneral.promote(); // SilverGeneral is promoted
        Pos silverPos = new Pos(4, 4); // Place in the middle of the board
        board.placePiece(silverGeneral, silverPos);

        // Act
        ArrayList<Pos> moves = silverGeneral.getAvailableMoves(silverPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(6, moves.size(), "Promoted SilverGeneral should have 6 valid moves like a GoldGeneral.");
        assertTrue(moves.contains(new Pos(3, 3)), "Promoted SilverGeneral can move to (3, 3).");
        assertTrue(moves.contains(new Pos(3, 5)), "Promoted SilverGeneral can move to (5, 3).");
        assertTrue(moves.contains(new Pos(3, 4)), "Promoted SilverGeneral can move forward to (3, 4).");
        assertTrue(moves.contains(new Pos(4, 3)), "Promoted SilverGeneral can move left to (4, 3).");
        assertTrue(moves.contains(new Pos(4, 5)), "Promoted SilverGeneral can move right to (4, 5).");
        assertTrue(moves.contains(new Pos(5, 4)), "Promoted SilverGeneral can move downward to (5, 4).");
    }

    @Test
    void testIsPromotedState() {
        // Arrange
        SilverGeneral silverGeneral = new SilverGeneral(Side.SENTE);

        // Act & Assert
        assertFalse(silverGeneral.getIsPromoted(), "SilverGeneral should not be promoted initially.");
        silverGeneral.promote();
        assertTrue(silverGeneral.getIsPromoted(), "SilverGeneral should be in the promoted state after calling promote().");
    }

    @Test
    void testCheckLegalMove() {
        // Arrange
        TestBoard board = new TestBoard();
        SilverGeneral silverGeneral = new SilverGeneral(Side.SENTE);
        Pos validPos = new Pos(3, 3);
        Pos invalidPos = new Pos(-1, -1); // Out of bounds

        // Act & Assert
        assertNotNull(silverGeneral.checkLegalMove(validPos, board), "Valid positions should be returned by checkLegalMove.");
        assertNull(silverGeneral.checkLegalMove(invalidPos, board), "Out-of-bounds positions should return null.");
    }
}