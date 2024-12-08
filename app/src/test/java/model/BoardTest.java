package model;

import model.pieces.Pawn;
import model.pieces.Piece;
import util.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Side;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(9, 9);
    }

    @Test
    void testInitialBoardState() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertNull(board.getPieceAt(new Pos(i, j)));
            }
        }
    }

    @Test
    void testMovePiece() {
        Piece piece = new Pawn(Side.GOTE);
        Pos from = new Pos(1, 1);
        Pos to = new Pos(2, 2);
        board.setAtPosition(from, piece);

        Move move = board.move(from, to);

        assertEquals(piece, board.getPieceAt(to));
        assertNull(board.getPieceAt(from));

        assertEquals(from, move.from());
        assertEquals(to, move.to());
        assertEquals(piece, move.movedPiece());
        assertNull(move.capturedPiece());
    }

    @Test
    void testGetBoardAsSfen() {
        Piece piece1 = new Pawn(Side.GOTE);
        Piece piece2 = new Pawn(Side.SENTE);
        board.setAtPosition(new Pos(0, 0), piece1);
        board.setAtPosition(new Pos(0, 1), piece2);
        String sfen = board.getBoardAsSfen();

        assertEquals("pP7/9/9/9/9/9/9/9/9", sfen);
    }

    @Test
    void testGetPieceAt() {
        Piece piece = new Pawn(Side.GOTE);
        Pos pos = new Pos(4, 4);
        board.setAtPosition(pos, piece);

        assertEquals(piece, board.getPieceAt(pos));
    }

    @Test
    void testInitializeBoard() {
        Sfen sfen = new Sfen("p8/9/9/9/9/9/9/9/8P b - 1");
        board.initializeBoard(sfen);

        assertNotNull(board.getPieceAt(new Pos(0, 0)));
        assertNotNull(board.getPieceAt(new Pos(8, 8)));
    }
}
