package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Pos;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    private History history;
    private Move move1;
    private Move move2;
    private Move move3;

    @BeforeEach
    void setUp() {
        history = new History();
        move1 = new Move(new Pos(0,0),new Pos(1,1),null,null);
        move2 = new Move(new Pos(5,3),new Pos(2,7),null,null);
        move3 = new Move(new Pos(4,4),new Pos(2,6),null,null);
    }

    @Test
    void testAddMoveAndGetNumberOfMoves() {
        assertEquals(0, history.getNumberOfMoves());
        history.addMove(move1);
        assertEquals(1, history.getNumberOfMoves());
        history.addMove(move2);
        assertEquals(2, history.getNumberOfMoves());
    }

    @Test
    void testGetMovesForwardRange() {
        history.addMove(move1);
        history.addMove(move2);
        history.addMove(move3);

        Iterator<Move> iterator = history.getMoves(0, 2);
        assertTrue(iterator.hasNext());
        assertEquals(move1, iterator.next());
        assertEquals(move2, iterator.next());
        assertEquals(move3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testGetMoves_BackwardRange() {
        history.addMove(move1);
        history.addMove(move2);
        history.addMove(move3);

        Iterator<Move> iterator = history.getMoves(2, 0);
        assertTrue(iterator.hasNext());
        assertEquals(move3, iterator.next());
        assertEquals(move2, iterator.next());
        assertEquals(move1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testGetMoves_InvalidIndices() {
        history.addMove(move1);
        history.addMove(move2);

        assertFalse(history.getMoves(-1, 1).hasNext());
        assertFalse(history.getMoves(0, 3).hasNext());
        assertFalse(history.getMoves(3, 0).hasNext());
    }

    @Test
    void testGetMoves_EmptyHistory() {
        assertFalse(history.getMoves(0, 0).hasNext());
    }

    @Test
    void testGetMoves_NoParameters() {
        history.addMove(move1);
        history.addMove(move2);
        history.addMove(move3);

        Iterator<Move> iterator = history.getMoves();
        assertTrue(iterator.hasNext());
        assertEquals(move1, iterator.next());
        assertEquals(move2, iterator.next());
        assertEquals(move3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testRemoveLast() {
        history.addMove(move1);
        history.addMove(move2);
        history.addMove(move3);

        assertEquals(move3, history.removeLast());
        assertEquals(2, history.getNumberOfMoves());
        assertEquals(move2, history.removeLast());
        assertEquals(1, history.getNumberOfMoves());
        assertEquals(move1, history.removeLast());
        assertEquals(0, history.getNumberOfMoves());
    }

    @Test
    void testRemoveLastEmptyHistory() {
        assertThrows(EmptyStackException.class, history::removeLast);
    }

    @Test
    void testGetLast() {
        history.addMove(move1);
        assertEquals(move1, history.getLast());

        history.addMove(move2);
        assertEquals(move2, history.getLast());

        history.addMove(move3);
        assertEquals(move3, history.getLast());
    }

    @Test
    void testGetLastEmptyHistory() {
        assertThrows(EmptyStackException.class, history::getLast);
    }
}