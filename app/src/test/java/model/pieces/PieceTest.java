package model.pieces;

import model.game.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Pos;
import util.Side;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    private Piece piece;
    private TestBoard board;

    @BeforeEach
    void setUp() {
        // Skapa en "MockPiece" som konkret subklass
        piece = new MockPiece(Side.SENTE);
        // Skapa en enkel testbräda istället för att använda Mockito
        board = new TestBoard(9, 9); // Exempelstorlek: 9x9
    }

    @Test
    void testGetSide() {
        assertEquals(Side.SENTE, piece.getSide());
    }

    @Test
    void testGetSfenAbbreviation_Sente() {
        assertEquals("M", piece.getSfenAbbreviation());
    }

    @Test
    void testGetSfenAbbreviation_Gote() {
        Piece gotePiece = new MockPiece(Side.GOTE);
        assertEquals("m", gotePiece.getSfenAbbreviation());
    }

    @Test
    void testCheckLegalMoveWithinBounds() {
        assertTrue(piece.checkLegalMoveWithinBounds(new Pos(5, 5), board));
        assertFalse(piece.checkLegalMoveWithinBounds(new Pos(9, 5), board)); // Utanför radgränserna
        assertFalse(piece.checkLegalMoveWithinBounds(new Pos(-1, 0), board)); // Negativa koordinater
    }

    @Test
    void testCheckLegalMoveNotCapturingOwnPiece() {
        Pos targetPos = new Pos(3, 3);

        // Placera en pjäs från samma sida
        board.placePiece(new MockPiece(Side.SENTE), targetPos);
        assertFalse(piece.checkLegalMoveNotCapturingOwnPiece(targetPos, board));

        // Placera en pjäs från motsatt sida
        board.placePiece(new MockPiece(Side.GOTE), targetPos);
        assertTrue(piece.checkLegalMoveNotCapturingOwnPiece(targetPos, board));

        // Ingen pjäs på målposition
        board.removePiece(targetPos);
        assertTrue(piece.checkLegalMoveNotCapturingOwnPiece(targetPos, board));
    }

    @Test
    void testCheckLegalMove() {
        Pos validMove = new Pos(5, 5);
        Pos outOfBoundsMove = new Pos(10, 10);
        Pos sameSideCaptureMove = new Pos(3, 3);

        // Scenario: Godkänd flytt
        assertEquals(validMove, piece.checkLegalMove(validMove, board));

        // Scenario: Utanför gränserna
        assertNull(piece.checkLegalMove(outOfBoundsMove, board));

        // Scenario: Försöker fånga egen pjäs
        board.placePiece(new MockPiece(Side.SENTE), sameSideCaptureMove);
        assertNull(piece.checkLegalMove(sameSideCaptureMove, board));
    }

    @Test
    void testGetForcingCheckMoves() {
        Pos kingPosition = new Pos(4, 4);
        ArrayList<Pos> moves = piece.getForcingCheckMoves(new Pos(3, 3), kingPosition, board);

        assertEquals(1, moves.size());
        assertTrue(moves.contains(kingPosition));
    }

    // MockPiece för att implementera abstrakta metoder i Piece
    private static class MockPiece extends Piece {
        public MockPiece(Side side) {
            super(side);
        }

        @Override
        public ArrayList<Pos> getAvailableMoves(Pos pos, Board board) {
            return new ArrayList<>();
        }

        @Override
        public ArrayList<Pos> getAvailableMovesBackend(Pos pos, Board board) {
            return new ArrayList<>();
        }
    }

    // Enkel "Board"-klass för tester
    private static class TestBoard extends Board {
        private final Piece[][] grid;

        public TestBoard(int width, int height) {
            super(width, height);
            grid = new Piece[height][width];
        }

        @Override
        public Piece getPieceAt(Pos pos) {
            if (pos.row() < 0 || pos.row() >= getHeight() || pos.col() < 0 || pos.col() >= getWidth()) {
                return null;
            }
            return grid[pos.row()][pos.col()];
        }

        public void placePiece(Piece piece, Pos pos) {
            if (pos.row() >= 0 && pos.row() < getHeight() && pos.col() >= 0 && pos.col() < getWidth()) {
                grid[pos.row()][pos.col()] = piece;
            }
        }

        public void removePiece(Pos pos) {
            if (pos.row() >= 0 && pos.row() < getHeight() && pos.col() >= 0 && pos.col() < getWidth()) {
                grid[pos.row()][pos.col()] = null;
            }
        }
    }
}