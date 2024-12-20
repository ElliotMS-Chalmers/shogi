package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    private static class TestBoard extends Board {
        private final Piece[][] board;

        public TestBoard() {
            super(8, 8); // 8x8 board
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
    void testGetSfenAbbreviation_SentePawn() {
        // Arrange
        Pawn pawn = new Pawn(Side.SENTE);

        // Act
        String abbreviation = pawn.getSfenAbbreviation();

        // Assert
        assertEquals("P", abbreviation, "SENTE Pawn should have SFEN abbreviation 'P'.");
    }

    @Test
    void testGetSfenAbbreviation_GotePawn() {
        // Arrange
        Pawn pawn = new Pawn(Side.GOTE);

        // Act
        String abbreviation = pawn.getSfenAbbreviation();

        // Assert
        assertEquals("p", abbreviation, "GOTE Pawn should have SFEN abbreviation 'p'.");
    }

    @Test
    void testGetAvailableMoves_SentePawn_NoObstacles() {
        // Arrange
        TestBoard board = new TestBoard();
        Pawn pawn = new Pawn(Side.SENTE);
        Pos pawnPos = new Pos(4, 4);
        board.placePiece(pawn, pawnPos);

        // Act
        ArrayList<Pos> moves = pawn.getAvailableMoves(pawnPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(1, moves.size(), "SENTE Pawn should have 1 valid move forward.");
        assertTrue(moves.contains(new Pos(3, 4)), "SENTE Pawn should move to (3,4).");
    }

    @Test
    void testGetAvailableMoves_GotePawn_NoObstacles() {
        // Arrange
        TestBoard board = new TestBoard();
        Pawn pawn = new Pawn(Side.GOTE);
        Pos pawnPos = new Pos(4, 4);
        board.placePiece(pawn, pawnPos);

        // Act
        ArrayList<Pos> moves = pawn.getAvailableMoves(pawnPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(1, moves.size(), "GOTE Pawn should have 1 valid move forward.");
        assertTrue(moves.contains(new Pos(5, 4)), "GOTE Pawn should move to (5,4).");
    }

    @Test
    void testGetAvailableMoves_SentePawn_BlockedByFriendlyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        Pawn pawn = new Pawn(Side.SENTE);
        Pawn friendlyPawn = new Pawn(Side.SENTE);
        Pos pawnPos = new Pos(4, 4);
        Pos blockingPos = new Pos(3, 4);
        board.placePiece(pawn, pawnPos);
        board.placePiece(friendlyPawn, blockingPos);

        // Act
        ArrayList<Pos> moves = pawn.getAvailableMoves(pawnPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(0, moves.size(), "SENTE Pawn should have no moves if blocked by a friendly piece.");
    }

    @Test
    void testGetAvailableMoves_SentePawn_BlockedByEnemyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        Pawn pawn = new Pawn(Side.SENTE);
        Pawn enemyPawn = new Pawn(Side.GOTE);
        Pos pawnPos = new Pos(4, 4);
        Pos blockingPos = new Pos(3, 4);
        board.placePiece(pawn, pawnPos);
        board.placePiece(enemyPawn, blockingPos);

        // Act
        ArrayList<Pos> moves = pawn.getAvailableMoves(pawnPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(1, moves.size(), "SENTE Pawn should capture an enemy piece directly in front.");
        assertTrue(moves.contains(new Pos(3, 4)), "SENTE Pawn should capture the enemy at (3,4).");
    }

    @Test
    void testGetAvailableMoves_GotePawn_BlockedAtBoardEdge() {
        // Arrange
        TestBoard board = new TestBoard();
        Pawn pawn = new Pawn(Side.GOTE);
        Pos pawnPos = new Pos(7, 4); // Bottom row
        board.placePiece(pawn, pawnPos);

        // Act
        ArrayList<Pos> moves = pawn.getAvailableMoves(pawnPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(0, moves.size(), "GOTE Pawn at the bottom edge of the board should have no moves.");
    }

    @Test
    void testGetAvailableMovesBackend_MatchesFrontend() {
        // Arrange
        TestBoard board = new TestBoard();
        Pawn pawn = new Pawn(Side.SENTE);
        Pos pawnPos = new Pos(4, 4);
        board.placePiece(pawn, pawnPos);

        // Act
        ArrayList<Pos> frontendMoves = pawn.getAvailableMoves(pawnPos, board);
        ArrayList<Pos> backendMoves = pawn.getAvailableMovesBackend(pawnPos, board);

        // Assert
        assertEquals(frontendMoves, backendMoves, "Frontend and backend moves should match for Pawn.");
    }

    @Test
    void testCheckLegalMove_ValidMove() {
        // Arrange
        TestBoard board = new TestBoard();
        Pawn pawn = new Pawn(Side.SENTE);
        Pos targetPos = new Pos(3, 4);

        // Act
        Pos result = pawn.checkLegalMove(targetPos, board);

        // Assert
        assertNotNull(result);
        assertEquals(targetPos, result, "checkLegalMove should return the same position for valid moves.");
    }

    @Test
    void testCheckLegalMove_OutOfBounds() {
        // Arrange
        TestBoard board = new TestBoard();
        Pawn pawn = new Pawn(Side.SENTE);

        // Act
        Pos result = pawn.checkLegalMove(new Pos(-1, 0), board);

        // Assert
        assertNull(result, "checkLegalMove should return null for out-of-bounds moves.");
    }

    @Test
    void testGetAvailableMovesBackend_PromotedPawn() {
        // Arrange
        TestBoard board = new TestBoard();
        Pawn pawn = new Pawn(Side.SENTE);
        pawn.promote(); // Promote the pawn
        Pos pawnPos = new Pos(4, 4);
        board.placePiece(pawn, pawnPos);

        // Act
        ArrayList<Pos> moves = pawn.getAvailableMovesBackend(pawnPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(6, moves.size(), "Promoted Pawn should have 6 moves available in all directions.");
        assertTrue(moves.contains(new Pos(3, 3)), "Promoted Pawn should move to (3,3).");
        assertTrue(moves.contains(new Pos(3, 4)), "Promoted Pawn should move to (3,4).");
        assertTrue(moves.contains(new Pos(3, 5)), "Promoted Pawn should move to (3,5).");
        assertTrue(moves.contains(new Pos(4, 3)), "Promoted Pawn should move to (4,3).");
        assertTrue(moves.contains(new Pos(4, 5)), "Promoted Pawn should move to (4,5).");
        assertTrue(moves.contains(new Pos(5, 4)), "Promoted Pawn should move to (5,4).");

    }
}