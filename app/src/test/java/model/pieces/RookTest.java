package model.pieces;

import model.game.Board;
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
    void testGetForcingCheckMove() {
        // Arrange
        TestBoard board = new TestBoard();
        Rook rook = new Rook(Side.SENTE);
        King king = new King(Side.GOTE);
        Pawn obstaclePawn = new Pawn(Side.SENTE);
        Pos rookPos = new Pos(4, 4);
        Pos kingPos = new Pos(4, 6);
        Pos obstaclePos = new Pos(4, 5);

        board.placePiece(rook, rookPos);
        board.placePiece(king, kingPos);

        // Act (Check for no obstacles)
        ArrayList<Pos> forcingMove = rook.getForcingCheckMoves(rookPos, kingPos, board);

        // Assert (No obstacle - Should find forcing move)
        assertNotNull(forcingMove, "Rook should have a forcing move to check the king.");
        assertTrue(forcingMove.contains(kingPos), "The forcing move should put the king in check.");

        // Add obstacle and test again
        board.placePiece(obstaclePawn, obstaclePos);

        // Act (Check with obstacle)
        forcingMove = rook.getForcingCheckMoves(rookPos, kingPos, board);

        // Assert (With obstacle - Should find no forcing move)
        assertNull(forcingMove, "Rook should not have a forcing move with an obstacle blocking the way.");
    }
    
    @Test
    void testGetForcingCheckMoves_PromotedRook() {
        // Arrange
        TestBoard board = new TestBoard();
        Rook rook = new Rook(Side.SENTE);
        King king = new King(Side.GOTE);
        rook.promote();
        Pos rookPos = new Pos(4, 4);
        Pos kingPos = new Pos(3, 3); // King on a diagonal


        board.placePiece(rook, rookPos);
        board.placePiece(king, kingPos);

        // Act
        ArrayList<Pos> forcingMoves = rook.getForcingCheckMoves(rookPos, kingPos, board);

        // Assert
        assertNotNull(forcingMoves, "Promoted rook should find forcing moves to check the king.");
        assertTrue(forcingMoves.contains(kingPos), "Promoted rook can check the king on a diagonal.");

    }

    @Test
    void testGetAvailableMovesBackend_PromotedRook() {
        // Arrange
        TestBoard board = new TestBoard();
        Rook rook = new Rook(Side.SENTE);
        rook.promote(); // Promote the rook
        Pos rookPos = new Pos(4, 4); // Center position on the board
        board.placePiece(rook, rookPos);

        // Act
        ArrayList<Pos> backendMoves = rook.getAvailableMovesBackend(rookPos, board);

        // Assert
        assertNotNull(backendMoves);
        assertEquals(18, backendMoves.size(), "Backend moves should match the frontend moves for a promoted rook.");
        assertTrue(backendMoves.contains(new Pos(3, 4)), "Promoted rook should allow move north.");
        assertTrue(backendMoves.contains(new Pos(4, 5)), "Promoted rook should allow move east.");
        assertTrue(backendMoves.contains(new Pos(3, 3)), "Promoted rook should allow diagonal move to (3,3).");
    }
    
}