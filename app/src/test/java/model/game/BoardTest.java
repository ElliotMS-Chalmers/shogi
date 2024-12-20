package model.game;

import model.Move;
import model.Sfen;
import model.pieces.*;
import util.Pos;
import org.junit.jupiter.api.Test;
import util.Side;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;



class BoardTest {

    @Test
    void testBoardInitialization() {
        Board board = new Board(9, 9);
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                assertNull(board.getPieceAt(new Pos(row, col)), "Position should be empty");
            }
        }
    }

    @Test
    void testPieceMovement() {
        Board board = new Board(9, 9);
        Piece pawn = new Pawn(Side.SENTE);
        board.setAtPosition(new Pos(2, 2), pawn);

        board.move(new Pos(2, 2), new Pos(3, 3));
        assertNull(board.getPieceAt(new Pos(2, 2)), "Original position should be empty");
        assertEquals(pawn, board.getPieceAt(new Pos(3, 3)), "Piece should have moved to the new position");
    }

    @Test
    void testCapturePiece() {
        Board board = new Board(9, 9);
        Piece sentePawn = new Pawn(Side.SENTE);
        Piece gotePawn = new Pawn(Side.GOTE);

        board.setAtPosition(new Pos(2, 2), sentePawn);
        board.setAtPosition(new Pos(3, 3), gotePawn);

        Move move = board.move(new Pos(2, 2), new Pos(3, 3));
        assertNull(board.getPieceAt(new Pos(2, 2)), "Original position should be empty");
        assertEquals(sentePawn, board.getPieceAt(new Pos(3, 3)), "Attacking piece should occupy the target position");
        assertEquals(gotePawn, move.capturedPiece(), "Captured piece should be recorded in the move");
    }

    @Test
    void testSetSfen() {
        Board board = new Board(9, 9);
        String sfen = "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL w - 1";
        Sfen sfenObj = new Sfen(sfen);

        board.setSfen(sfenObj);
        assertEquals("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL", board.getBoardAsSfen());
    }

    @Test
    void testGetEveryPiece() {
        Board board = new Board(9, 9);

        board.setAtPosition(new Pos(0, 0), new King(Side.SENTE));
        board.setAtPosition(new Pos(1, 1), new Pawn(Side.GOTE));

        ArrayList<Piece> pieces = board.getEveryPiece();
        assertEquals(2, pieces.size(), "There should be two pieces on the board");
    }

    @Test
    void testGetEveryPiecePos() {
        // Create a 9x9 board
        Board board = new Board(9, 9);

        // Add some pieces to the board
        board.setAtPosition(new Pos(0, 0), new King(Side.SENTE)); // Position (0,0)
        board.setAtPosition(new Pos(4, 3), new Pawn(Side.GOTE)); // Position (4,3)
        board.setAtPosition(new Pos(8, 8), new Rook(Side.SENTE)); // Position (8,8)

        // Call getEveryPiecePos
        ArrayList<Pos> positions = board.getEveryPiecePos();

        // Verify the results
        assertEquals(3, positions.size(), "There should be three pieces on the board");
        assertTrue(positions.contains(new Pos(0, 0)), "Position (0,0) should be in the list");
        assertTrue(positions.contains(new Pos(4, 3)), "Position (4,3) should be in the list");
        assertTrue(positions.contains(new Pos(8, 8)), "Position (8,8) should be in the list");
    }

    @Test
    void testTestMove() {

        Board board = new Board(9, 9);

        Pos from = new Pos(0, 0);
        Pos to = new Pos(1, 1);
        Piece king = new King(Side.SENTE);
        Piece pawn = new Pawn(Side.GOTE);

        board.setAtPosition(from, king);
        board.setAtPosition(to, pawn);

        Piece capturedPiece = board.testMove(from, to, null);

        assertEquals(pawn, capturedPiece, "The captured piece should be the Pawn at position (1,1)");

        assertEquals(king, board.getPieceAt(to), "The King should be moved to position (1,1)");
        assertNull(board.getPieceAt(from), "The original position (0,0) should now be empty");

        assertNull(board.getPieceAt(from), "The position (0,0) should have the passed piece (null) placed");

        Pos emptyTo = new Pos(2, 2);
        board.setAtPosition(from, king);
        board.setAtPosition(emptyTo, null);

        Piece noCapture = board.testMove(from, emptyTo, null);
        assertNull(noCapture, "No piece should be captured at an empty destination");

        assertEquals(king, board.getPieceAt(emptyTo), "The King should be moved to position (2,2)");
        assertNull(board.getPieceAt(from), "The original position (0,0) should now be empty");
    }

    @Test
    void testGetInvalidPiecePos() {
        assertNull(new Board(9,9).getPiecePos(Side.SENTE, Pawn.class));
    }

    @Test
    void testGetPiecePosition() {
        Board board = new Board(9, 9);

        Piece king = new King(Side.SENTE);
        board.setAtPosition(new Pos(0, 0), king);

        Pos kingPos = board.getPiecePos(Side.SENTE, King.class);
        assertEquals(new Pos(0, 0), kingPos, "King position should be found");
    }

    @Test
    void testGetIfPieceInColum() {
        Board board = new Board(9, 9);

        int targetColumn = 4;
        King senteKing = new King(Side.SENTE);
        Pawn gotePawn = new Pawn(Side.GOTE);

        board.setAtPosition(new Pos(3, targetColumn), senteKing);
        board.setAtPosition(new Pos(5, 5), gotePawn);

        assertTrue(board.ifPieceInColum(Side.SENTE, King.class, targetColumn),
                "There should be a Sente King in column 4");

        assertFalse(board.ifPieceInColum(Side.GOTE, King.class, targetColumn),
                "There should not be a Gote King in column 4");

        assertFalse(board.ifPieceInColum(Side.SENTE, King.class, 5),
                "There should not be a Sente King in column 5");

        assertFalse(board.ifPieceInColum(Side.GOTE, Pawn.class, 6),
                "There should not be any piece in column 6");
    }
}
