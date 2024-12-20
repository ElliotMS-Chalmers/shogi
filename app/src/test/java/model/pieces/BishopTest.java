package model.pieces;

import model.game.Board;
import util.Pos;
import util.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {

    private static class TestBoard extends Board {
        private final Piece[][] board;

        public TestBoard() {
            super(8, 8); // Skapar ett standardbräde på 8x8
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
        Bishop bishop = new Bishop(Side.SENTE);
        Pos bishopPos = new Pos(4, 4); // Biskopen är placerad mitt på brädet

        // Act
        ArrayList<Pos> moves = bishop.getAvailableMoves(bishopPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(13, moves.size(), "Bishop should have 13 valid moves on an empty board from the center.");
        assertTrue(moves.contains(new Pos(3, 3)), "Should include top-left diagonal move.");
        assertTrue(moves.contains(new Pos(7, 7)), "Should include bottom-right diagonal move.");
        assertTrue(moves.contains(new Pos(0, 0)), "Should include corner (top-left) diagonal move.");
    }

    @Test
    void testGetAvailableMoves_BlockedByFriendlyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        Bishop bishop = new Bishop(Side.SENTE);
        Pawn friendlyPawn = new Pawn(Side.SENTE);
        Pos bishopPos = new Pos(4, 4);
        Pos blockingPos = new Pos(5, 5);

        board.placePiece(bishop, bishopPos);
        board.placePiece(friendlyPawn, blockingPos); // Blocking the bishop's diagonal path

        // Act
        ArrayList<Pos> moves = bishop.getAvailableMoves(bishopPos, board);

        // Assert
        assertNotNull(moves);
        assertFalse(moves.contains(new Pos(5, 5)), "Bishop cannot move to square occupied by friendly piece.");
        assertFalse(moves.contains(new Pos(6, 6)), "Bishop cannot move past a friendly piece.");
    }

    @Test
    void testGetAvailableMoves_BlockedByEnemyPiece() {
        // Arrange
        TestBoard board = new TestBoard();
        Bishop bishop = new Bishop(Side.SENTE);
        Pawn enemyPawn = new Pawn(Side.GOTE);
        Pos bishopPos = new Pos(4, 4);
        Pos enemyPos = new Pos(5, 5);

        board.placePiece(bishop, bishopPos);
        board.placePiece(enemyPawn, enemyPos); // Enemy piece blocking path diagonal

        // Act
        ArrayList<Pos> moves = bishop.getAvailableMoves(bishopPos, board);

        // Assert
        assertNotNull(moves);
        assertTrue(moves.contains(new Pos(5, 5)), "Bishop can capture an enemy piece.");
        assertFalse(moves.contains(new Pos(6, 6)), "Bishop cannot move past an enemy piece.");
    }

    @Test
    void testGetAvailableMoves_Promoted() {
        // Arrange
        TestBoard board = new TestBoard();
        Bishop bishop = new Bishop(Side.SENTE);
        bishop.promote(); // Promote the bishop
        Pos bishopPos = new Pos(4, 4);

        // Act
        ArrayList<Pos> moves = bishop.getAvailableMoves(bishopPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(17, moves.size(), "Promoted Bishop should have 13 diagonal and 4 orthogonal moves.");
        assertTrue(moves.contains(new Pos(3, 4)), "Promoted Bishop can move forward.");
        assertTrue(moves.contains(new Pos(4, 3)), "Promoted Bishop can move to the left.");
    }

    @Test
    void testGetAvailableMovesBackend_CapturingEnemyKing() {
        // Arrange
        TestBoard board = new TestBoard();
        Bishop bishop = new Bishop(Side.SENTE);
        King enemyKing = new King(Side.GOTE);
        Pos bishopPos = new Pos(4, 4);
        Pos kingPos = new Pos(5, 5);

        board.placePiece(bishop, bishopPos);
        board.placePiece(enemyKing, kingPos);

        // Act
        ArrayList<Pos> moves = bishop.getAvailableMovesBackend(bishopPos, board);

        // Assert
        assertNotNull(moves);
        assertTrue(moves.contains(new Pos(5, 5)), "Backend logic should allow capturing the enemy King.");
    }

    @Test
    void testGetAvailableMovesBackend_BlockByEnemyKing() {
        // Arrange
        TestBoard board = new TestBoard();
        Bishop bishop = new Bishop(Side.SENTE);
        King enemyKing = new King(Side.GOTE);
        Pos bishopPos = new Pos(4, 4);
        Pos kingPos = new Pos(5, 5);

        board.placePiece(bishop, bishopPos);
        board.placePiece(enemyKing, kingPos);

        // Act
        ArrayList<Pos> moves = bishop.getAvailableMovesBackend(bishopPos, board);

        // Assert
        assertNotNull(moves);
        assertTrue(moves.contains(new Pos(5, 5)), "Bishop can capture the enemy king in backend logic.");
    }

    @Test
    void testGetAvailableMovesBackend_Promoted() {
        // Arrange
        TestBoard board = new TestBoard();
        Bishop bishop = new Bishop(Side.SENTE);
        bishop.promote(); // Promote the bishop
        Pos bishopPos = new Pos(4, 4);

        // Act
        ArrayList<Pos> moves = bishop.getAvailableMovesBackend(bishopPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(17, moves.size(), "Promoted Bishop should have 13 diagonal and 4 orthogonal moves.");
        assertTrue(moves.contains(new Pos(3, 4)), "Promoted Bishop can move forward.");
        assertTrue(moves.contains(new Pos(4, 3)), "Promoted Bishop can move to the left.");
    }

    @Test
    void testGetForcingCheckMoves() {
        // Arrange
        TestBoard board = new TestBoard();
        Bishop bishop = new Bishop(Side.SENTE);
        King enemyKing = new King(Side.GOTE);
        Pos bishopPos = new Pos(4, 4);
        Pos kingPos = new Pos(2, 2);

        board.placePiece(bishop, bishopPos);
        board.placePiece(enemyKing, kingPos);

        // Act
        ArrayList<Pos> moves = bishop.getForcingCheckMoves(bishopPos, kingPos, board);

        // Assert
        assertNotNull(moves, "Forcing check moves should not be null.");
        assertTrue(moves.contains(new Pos(2, 2)), "Bishop should have a move that puts the enemy King in check.");
        assertFalse(moves.contains(new Pos(6, 6)), "Bishop should not include moves that do not lead to a check.");
    }

    @Test
    void testGetForcingCheckMoves_Promoted() {
        // Arrange
        TestBoard board = new TestBoard();
        Bishop bishop = new Bishop(Side.SENTE);
        bishop.promote(); // Promote the bishop
        King enemyKing = new King(Side.GOTE);
        Pos bishopPos = new Pos(4, 4);
        Pos kingPos = new Pos(3, 4); // Position for forcing check orthogonally

        board.placePiece(bishop, bishopPos);
        board.placePiece(enemyKing, kingPos);

        // Act
        ArrayList<Pos> moves = bishop.getForcingCheckMoves(bishopPos, kingPos, board);

        // Assert
        assertNotNull(moves, "Forcing check moves should not be null for a promoted bishop.");
        assertTrue(moves.contains(new Pos(3, 4)), "Promoted bishop should have moves that force a check orthogonally.");
    }

}