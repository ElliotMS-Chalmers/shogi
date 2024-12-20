package model;

import org.junit.jupiter.api.Test;
import util.Pos;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SfenTest {

    @Test
    void testConstructorWithValidSfenString() {
        Sfen sfen = new Sfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1");
        assertEquals("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL", sfen.getBoardPosition());
        assertEquals('b', sfen.getTurn());
        assertEquals("-", sfen.getCapturedPieces());
        assertEquals(1, sfen.getMoveCount());
    }

    @Test
    void testConstructorWithInvalidSfenString() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Sfen("invalid sfen");
        });
    }

    @Test
    void testToString() {
        Sfen sfen = new Sfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL", 'b', "-", 1);
        assertEquals("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1", sfen.toString());
    }

    @Test
    void testForEachPiece() {
        Sfen sfen = new Sfen("lnsgkgsnl/1r5b1/pppp+ppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1");
        List<String> pieces = new ArrayList<>();
        List<Pos> positions = new ArrayList<>();

        sfen.forEachPiece((piece, pos) -> {
            pieces.add(piece);
            positions.add(pos);
        });

        assertEquals(40, pieces.size()); // Total number of pieces on the board
        assertTrue(pieces.contains("l")); // Check that a specific piece is present
        assertEquals(new Pos(0, 0), positions.get(0)); // Check a specific position
    }

    @Test
    void testForEachCapturedPiece() {
        Sfen sfen = new Sfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b 2Pp 1");
        List<Character> pieces = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        sfen.forEachCapturedPiece((piece, count) -> {
            pieces.add(piece);
            counts.add(count);
        });

        assertEquals(2, pieces.size());
        assertTrue(pieces.contains('P'));
        assertTrue(pieces.contains('p'));
        assertEquals(2, counts.get(0)); // Check the number of captured pieces
    }

    @Test
    void testSettersAndGetters() {
        Sfen sfen = new Sfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1");
        sfen.setBoardPosition("newBoard");
        assertEquals("newBoard", sfen.getBoardPosition());

        sfen.setTurn('w');
        assertEquals('w', sfen.getTurn());

        sfen.setCapturedPieces("Pp");
        assertEquals("Pp", sfen.getCapturedPieces());

        sfen.setMoveCount(5);
        assertEquals(5, sfen.getMoveCount());
    }
}
