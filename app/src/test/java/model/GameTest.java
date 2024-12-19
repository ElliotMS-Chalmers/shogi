package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.BooleanProperty;
import model.pieces.GoldGeneral;
import model.pieces.Pawn;
import model.pieces.Piece;
import model.pieces.Promotable;
import model.variants.Standard;
import model.variants.Variant;
import util.Pos;
import util.Side;

public class GameTest {

    private Game game;
    private Variant variant;

    @BeforeEach
    void setUp() {
        variant = new Standard();  // Assume a simple variant constructor
        game = new Game(variant, 0);  // Time limit of 0 for no clocks
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game.getBoard(), "Board should be initialized.");
        assertNotNull(game.getHistory(), "History should be initialized.");
        assertEquals(Side.SENTE, game.getTurn(), "Starting turn should be SENTE.");
        assertTrue(game.getGameRunning().get(), "Game should be running.");
    }

    @Test
    void testClockInitializationAndStart() {
        Game gameWithClock = new Game(variant, 10);  // Time limit of 10 seconds

        assertTrue(gameWithClock.isClocksInitialized(), "Both clocks should be initialized.");
        assertEquals(10, gameWithClock.getTime(Side.SENTE), "Sente's time should be 10 seconds."); // SENTE's clock is not paused will decrement once before we use getTime
        assertEquals(10, gameWithClock.getTime(Side.GOTE), "Gote's time should be 10 seconds.");
    }
    @Test
    void testMoveExecution() {
        Pos from = new Pos(6, 0);
        Pos to = new Pos(5, 0);  

        Piece piece = new Pawn(Side.SENTE); 
        game.getBoard().setAtPosition(from, piece); 
        assertNotNull(game.move(from, to), "Move should be executed.");

        // Verify that the turn has changed after the move
        assertEquals(Side.GOTE, game.getTurn(), "Turn should change after a valid move.");

        // Verify that the move count has increased
        assertEquals(2, game.getMoveCount(), "Move count should increase.");
    }

    @Test
    void testInvalidMove() {
        Pos from = new Pos(6, 0);  // A valid position
        Pos to = new Pos(7, 0);    // Assuming invalid position (out of bounds or occupied)
    
        Piece piece = new Pawn(Side.SENTE); 
        game.getBoard().setAtPosition(from, piece);
        assertNull(game.move(from, to), "Move should be invalid and return null.");
    }

    @Test
    void testNonPromotablePiece() {
        Pos promotionPos = new Pos(0, 0);  // Assuming an invalid position for promotion (non-promotable piece)
        game.getBoard().setAtPosition(promotionPos, new GoldGeneral(Side.SENTE));
        Piece piece = game.getBoard().getPieceAt(promotionPos);
    
        assertFalse(piece instanceof Promotable, "Piece should not be promotable.");
        game.promotePieceAt(promotionPos);
        assertFalse(piece instanceof Promotable, "Non-promotable piece should not be promoted.");
    }

    @Test
    void testUndoMove() {
        Pos from = new Pos(6, 0);  // Assume a valid position
        Pos to = new Pos(5, 0);    // Assume a valid destination

        game.move(from, to);  // Execute a move
        assertEquals(2, game.getMoveCount(), "Move count should be 2 after move.");

        game.undo();  // Undo the move
        assertEquals(1, game.getMoveCount(), "Move count should be 1 after undo.");
    }

    @Test
    void testGameStartWithZeroTimeLimit() {
        Game gameWithZeroTime = new Game(variant, 0);  // Zero time limit
    
        // Ensure that the game starts with no clock and time remains at 0
        assertFalse(gameWithZeroTime.isClocksInitialized(), "Clocks should not be initialized with zero time.");
        assertEquals(0, gameWithZeroTime.getTime(Side.SENTE), "Sente's time should be 0.");
        assertEquals(0, gameWithZeroTime.getTime(Side.GOTE), "Gote's time should be 0.");
    }

    @Test
    void testBoardChangedAfterMove() {
        Pos from = new Pos(6, 0);
        Pos to = new Pos(5, 0);  
    
        Piece piece = new Pawn(Side.SENTE); 
        game.getBoard().setAtPosition(from, piece); 
        BooleanProperty boardChanged = game.boardChangedProperty();
        boardChanged.set(false);
    
        // Make a move and check if boardChanged property is updated
        game.move(from, to);
        assertTrue(boardChanged.get(), "BoardChanged should be true after a move.");
    }

    @Test
    void testGameShutdown() {
        Game game = new Game(variant, 10);  // 10 seconds time limit

        // Simulate game shutdown
        game.stopClock();

        assertFalse(game.getGameRunning().get(), "Game should not be running after stopping the clock.");
    }

    @Test
    void testChangeTurn() {

        assertEquals(Side.SENTE, game.getTurn(), "Initial turn should be SENTE.");
        game.move(new Pos(6, 0), new Pos(5, 0));  // Assume a valid move
        assertEquals(Side.GOTE, game.getTurn(), "Turn should change to GOTE after the move.");
    }

    @Test
    void testBoardChangedProperty() {  

        BooleanProperty boardChanged = game.boardChangedProperty();

        assertFalse(boardChanged.get(), "The boardChanged property should initially be false.");

        boardChanged.set(true);
        assertTrue(boardChanged.get(), "The boardChanged property should be true after being set.");
    }

    @Test
    void testGetVariant() {
        assertEquals(variant, game.getVariant(), "The variant returned should match the mock variant.");
    }


    @Test
    void testPromotePiece() {
        Pos promotionPos = new Pos(6, 6);  // Assume this is a valid promotion position for a piece

        Piece piece = game.getBoard().getPieceAt(promotionPos);
        assertTrue(piece instanceof Promotable, "Piece should not be promotable before promotion.");

        game.promotePieceAt(promotionPos);
        piece = game.getBoard().getPieceAt(promotionPos);
        assertFalse(((Promotable) piece).getIsPromoted(), "Piece should not be promoted after promotion.");

        game.getBoard().setAtPosition(promotionPos, new Pawn(Side.GOTE));
        piece = game.getBoard().getPieceAt(promotionPos);
        game.promotePieceAt(promotionPos);
        assertTrue(((Promotable) piece).getIsPromoted(), "Piece should be promoted after promotion.");
    }
}