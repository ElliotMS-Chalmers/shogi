package model.pieces;

import model.Board;
import util.Pos;
import util.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

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
    void testGetAvailableMoves_Unpromoted_NoObstacles() {
        // Arrange
        TestBoard board = new TestBoard();
        Rook rook = new Rook(Side.SENTE);
        Pos rookPos = new Pos(4, 4); // Central position
        board.placePiece(rook, rookPos);

        // Act
        ArrayList<Pos> moves = rook.getAvailableMoves(rookPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(14, moves.size(), "Unpromoted Rook should have 14 possible moves without obstacles.");
        // Vertical and horizontal moves
        assertTrue(moves.contains(new Pos(3, 4))); // North
        assertTrue(moves.contains(new Pos(5, 4))); // South
        assertTrue(moves.contains(new Pos(4, 3))); // West
        assertTrue(moves.contains(new Pos(4, 5))); // East
    }

    @Test
    void testGetAvailableMoves_Unpromoted_WithObstacles() {
        // Arrange
        TestBoard board = new TestBoard();
        Rook rook = new Rook(Side.SENTE);
        Pawn friendlyPawn = new Pawn(Side.SENTE);
        Pawn enemyPawn = new Pawn(Side.GOTE);
        Pos rookPos = new Pos(4, 4);
        Pos friendPos = new Pos(4, 6); // Friendly piece to the east
        Pos enemyPos = new Pos(2, 4); // Enemy piece to the north
        board.placePiece(rook, rookPos);
        board.placePiece(friendlyPawn, friendPos);
        board.placePiece(enemyPawn, enemyPos);

        // Act
        ArrayList<Pos> moves = rook.getAvailableMoves(rookPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(10, moves.size(), "Unpromoted Rook should stop at friendly pieces and capture enemy pieces.");
        assertTrue(moves.contains(new Pos(3, 4)), "Rook can move to (3,4).");
        assertTrue(moves.contains(new Pos(2, 4)), "Rook can capture the enemy pawn at (2,4).");
        assertFalse(moves.contains(new Pos(1, 4)), "Rook cannot move past enemy pieces.");
        assertFalse(moves.contains(new Pos(4, 6)), "Rook cannot move where the friendly pawn is positioned.");
        assertTrue(moves.contains(new Pos(4, 5)), "Rook can move to (4,5) before the friendly pawn.");
    }

    @Test
    void testGetAvailableMoves_Promoted_NoObstacles() {
        // Arrange
        TestBoard board = new TestBoard();
        Rook rook = new Rook(Side.SENTE);
        rook.promote(); // Promote the rook
        Pos rookPos = new Pos(4, 4); // Central position
        board.placePiece(rook, rookPos);

        // Act
        ArrayList<Pos> moves = rook.getAvailableMoves(rookPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(18, moves.size(), "Promoted Rook should have 22 moves (14 line + 4 diagonals).");
        // Line moves
        assertTrue(moves.contains(new Pos(3, 4))); // North
        assertTrue(moves.contains(new Pos(5, 4))); // South
        // Diagonal moves
        assertTrue(moves.contains(new Pos(3, 3)), "Promoted Rook can move to (3,3) diagonally.");
        assertTrue(moves.contains(new Pos(5, 5)), "Promoted Rook can move to (5,5) diagonally.");
    }

    @Test
    void testGetAvailableMoves_Promoted_WithObstacles() {
        // Arrange
        TestBoard board = new TestBoard();
        Rook rook = new Rook(Side.SENTE);
        Pawn friendlyPawn = new Pawn(Side.SENTE);
        Pawn enemyPawn = new Pawn(Side.GOTE);
        rook.promote();
        Pos rookPos = new Pos(4, 4);
        Pos friendPos = new Pos(3, 3); // Friendly piece on the diagonal
        Pos enemyPos = new Pos(4, 6); // Enemy piece on another diagonal
        board.placePiece(rook, rookPos);
        board.placePiece(friendlyPawn, friendPos);
        board.placePiece(enemyPawn, enemyPos);

        // Act
        ArrayList<Pos> moves = rook.getAvailableMoves(rookPos, board);

        // Assert
        assertNotNull(moves);
        assertEquals(16, moves.size(), "Promoted Rook can stop at friendly pieces and capture enemy pieces diagonally.");
        assertFalse(moves.contains(new Pos(3, 3)), "Promoted Rook cannot move where a friendly pawn is located.");
        assertTrue(moves.contains(enemyPos), "Promoted Rook can capture an enemy pawn located at (4,6).");
        assertFalse(moves.contains(new Pos(enemyPos.row(), enemyPos.col()+1)), "Promoted Rook cannot move past an enemy piece.");
    }

    @Test
    void testGetAvailableMovesBackend_SimpleCase() {
        // Arrange
        TestBoard board = new TestBoard();
        Rook rook = new Rook(Side.SENTE);
        Pos rookPos = new Pos(4, 4);
        board.placePiece(rook, rookPos);

        // Act
        ArrayList<Pos> backendMoves = rook.getAvailableMovesBackend(rookPos, board);

        // Assert
        assertNotNull(backendMoves);
        assertEquals(14, backendMoves.size(), "Backend moves should match the frontend unpromoted Rook moves.");
    }

    @Test
    void testIsPromotedState() {
        // Arrange
        Rook rook = new Rook(Side.SENTE);

        // Act & Assert
        assertFalse(rook.getIsPromoted(), "Rook should not start in the promoted state.");
        rook.promote();
        assertTrue(rook.getIsPromoted(), "Rook should be promoted after calling promote().");
    }

    @Test
    void testCheckLegalMove() {
        // Arrange
        TestBoard board = new TestBoard();
        Rook rook = new Rook(Side.SENTE);
        Pos validMove = new Pos(3, 4);
        Pos invalidMove = new Pos(-1, -1);

        // Act & Assert
        assertNotNull(rook.checkLegalMove(validMove, board), "Valid moves within bounds should be returned.");
        assertNull(rook.checkLegalMove(invalidMove, board), "Out-of-bounds moves should return null.");
    }

    @Test
    void testGetAvailableMovesBackend_PromotedRook() {
        // Arrange
        TestBoard board = new TestBoard();
        Rook rook = new Rook(Side.SENTE);
        rook.promote(); // Promote the rook
        Pos rookPos = new Pos(4, 4); // Central position
        board.placePiece(rook, rookPos);

        // Act
        ArrayList<Pos> moves = rook.getAvailableMovesBackend(rookPos, board);

        // Assert
        assertNotNull(moves);
        // 14 moves for horizontal/vertical (unpromoted rook) + 4 diagonal moves
        assertEquals(18, moves.size(), "Promoted Rook should have 18 possible moves.");

        // Assert horizontal and vertical moves
        assertTrue(moves.contains(new Pos(3, 4)), "Promoted Rook can move vertically to (3,4).");
        assertTrue(moves.contains(new Pos(5, 4)), "Promoted Rook can move vertically to (5,4).");
        assertTrue(moves.contains(new Pos(4, 3)), "Promoted Rook can move horizontally to (4,3).");
        assertTrue(moves.contains(new Pos(4, 5)), "Promoted Rook can move horizontally to (4,5).");

        // Assert diagonal moves (unique to promoted rook)
        assertTrue(moves.contains(new Pos(3, 3)), "Promoted Rook can move diagonally to (3,3).");
        assertTrue(moves.contains(new Pos(3, 5)), "Promoted Rook can move diagonally to (3,5).");
        assertTrue(moves.contains(new Pos(5, 3)), "Promoted Rook can move diagonally to (5,3).");
        assertTrue(moves.contains(new Pos(5, 5)), "Promoted Rook can move diagonally to (5,5).");
    }

    @Test
    void testGetAvailableMovesBackend_BlockByEnemyKing() {
        // Arrange
        TestBoard board = new TestBoard();
        Rook rook = new Rook(Side.SENTE); // Sente Rook
        King enemyKing = new King(Side.GOTE); // Gote King
        Pos rookPos = new Pos(4, 4);
        Pos kingPos = new Pos(4, 6); // Enemy King directly in rook's path
        board.placePiece(rook, rookPos);
        board.placePiece(enemyKing, kingPos);

        // Act
        ArrayList<Pos> moves = rook.getAvailableMovesBackend(rookPos, board);

        // Assert
        assertNotNull(moves);
        assertTrue(moves.contains(kingPos), "Rook should be able to move to the enemy King's position.");
        assertTrue(moves.contains(new Pos(4, 7)), "Rook should pass through the enemy King in the backend.");
        assertFalse(moves.contains(new Pos(4, 8)), "Rook cannot continue movement past the enemy King.");
    }
}