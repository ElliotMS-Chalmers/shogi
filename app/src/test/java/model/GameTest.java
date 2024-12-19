package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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
        gameWithClock.getClock(Side.SENTE).pause();

        assertTrue(gameWithClock.isClocksInitialized(), "Both clocks should be initialized.");
        assertEquals(10, gameWithClock.getTime(Side.SENTE), "Sente's time should be 10 seconds.");
        assertEquals(10, gameWithClock.getTime(Side.GOTE), "Gote's time should be 10 seconds.");
    }
    @Test
    void testMoveExecution() {

        // SENTE
        Pos from = new Pos(6, 0);
        Pos to = new Pos(5, 0);  

        Piece piece = new Pawn(Side.SENTE); 
        game.getBoard().setAtPosition(from, piece); 
        assertNotNull(game.move(from, to), "Move should be executed.");

        // Verify that the turn has changed after the move
        assertEquals(Side.GOTE, game.getTurn(), "Turn should change after a valid move.");

        // Verify that the move count has increased
        assertEquals(2, game.getMoveCount(), "Move count should increase.");

        // GOTE

        from = new Pos(2, 0);
        to = new Pos(3, 0);  

        piece = new Pawn(Side.GOTE); 
        game.getBoard().setAtPosition(from, piece); 
        assertNotNull(game.move(from, to), "Move should be executed.");

        // Verify that the turn has changed after the move
        assertEquals(Side.SENTE, game.getTurn(), "Turn should change after a valid move.");

        // Verify that the move count has increased
        assertEquals(3, game.getMoveCount(), "Move count should increase.");
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
    void testChangeActiveClock_SenteToGote() {
        game = new Game(variant, 360);
        // Initialize clocks
        Clock senteClock = game.getClock(Side.SENTE);
        Clock goteClock = game.getClock(Side.GOTE);
    
        // Ensure initial state: Sente's turn
        assertFalse(senteClock.isPaused());
        assertTrue(goteClock.isPaused());
    
        // Change turn
        game.move(new Pos(6,0), new Pos(5,0));
    
        // Assert clock state after turn change
        assertTrue(senteClock.isPaused());
        assertFalse(goteClock.isPaused());

        game.move(new Pos(5,0), new Pos(4, 0));

        assertFalse(senteClock.isPaused());
        assertTrue(goteClock.isPaused());
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
    void testUndoCaptureMove() {
        Pos from = new Pos(5, 0);
        Pos to = new Pos(4, 0);

        // Place a piece for sente and gote
        Piece sentePiece = new Pawn(Side.SENTE);
        Piece gotePiece = new Pawn(Side.GOTE);
        game.getBoard().setAtPosition(new Pos(2,0), null);
        game.getBoard().setAtPosition(new Pos(6, 0), null);
        game.getBoard().setAtPosition(from, sentePiece);
        game.getBoard().setAtPosition(to, gotePiece);

        // Make a move that captures
        game.move(from, to);

        // Assert capture occurred
        assertNull(game.getBoard().getPieceAt(from));
        assertEquals(sentePiece, game.getBoard().getPieceAt(to));
        assertTrue(game.getPlayer(Side.SENTE).getHand().containsKey(Pawn.class));

        // Undo the move
        game.undo();

        // Assert move and capture were undone
        assertEquals(sentePiece, game.getBoard().getPieceAt(from));
        assertEquals(gotePiece, game.getBoard().getPieceAt(to));
        assertFalse(game.getPlayer(Side.SENTE).getHand().get(Pawn.class) != 0);
        assertEquals(1, game.getMoveCount());
    }

    @Test
    void testUndoMoveFromHand() {
        Pos to = new Pos(4, 0);

        // Add a piece to sente's hand
        Piece sentePiece = new Pawn(Side.SENTE);
        game.getPlayer(Side.SENTE).addCapturedPiece(sentePiece.getClass());

        // Place the piece from hand
        game.getBoard().setAtPosition(new Pos(6,0), null);
        game.playHand(to, sentePiece);
        System.out.println(game.getPlayer(Side.SENTE).getHand().get(Pawn.class));

        // Assert piece was placed
        assertEquals(sentePiece.getClass(), game.getBoard().getPieceAt(to).getClass());
        assertFalse(game.getPlayer(Side.SENTE).getHand().get(Pawn.class) != 0);

        // Undo the move
        game.undo();

        // Assert move was undone
        assertNull(game.getBoard().getPieceAt(to));
        assertTrue(game.getPlayer(Side.SENTE).getHand().get(Pawn.class) != 0);
        assertEquals(1, game.getMoveCount());
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
    void testValidHandMoveSENTE() {
        //SENTE

        // Set up a valid position and piece
        Pos validPos = new Pos(5, 0); // Assuming this is a valid position for the hand move
        Piece validPiece = new Pawn(Side.SENTE); // Assuming this is a valid piece for the hand move
    
        // Initialize player's hand with the piece
        game.getPlayer(Side.SENTE).intializeHand(List.of(validPiece.getClass()));
    
        // The initial state
        int initialMoveCount = game.getMoveCount();
        Side initialTurn = game.getTurn();
        int initialCapturedPieceCount = game.getPlayer(Side.SENTE).getHand().get(validPiece.getClass());
    
        // Play the hand
        game.getBoard().setAtPosition(new Pos(6,0), null); // Remove one piece from board
        game.playHand(validPos, validPiece);
    
        // Verify piece placement
        assertEquals(validPiece, game.getBoard().getPieceAt(validPos), "Piece should be placed on the board at the specified position.");
    
        // Verify turn change
        assertNotEquals(initialTurn, game.getTurn(), "Turn should change after a valid move.");
    
        // Verify move count increase
        assertEquals(initialMoveCount + 1, game.getMoveCount(), "Move count should increase by 1.");
    
        // Verify history update
        assertEquals(initialMoveCount, game.getHistory().getNumberOfMoves(), "History should have the same number of moves as the move count.");
    
        // Verify that the captured pieces of the player are updated
        assertEquals(initialCapturedPieceCount - 1, game.getPlayer(Side.SENTE).getHand().get(validPiece.getClass()), "Sente's captured pieces should be updated.");
        
        // Verify board change
        assertTrue(game.boardChangedProperty().get(), "Board should be marked as changed.");

        //GOTE

        // Set up a valid position and piece
        validPos = new Pos(3, 0); // Assuming this is a valid position for the hand move
        validPiece = new Pawn(Side.GOTE); // Assuming this is a valid piece for the hand move
    
        // Initialize player's hand with the piece
        game.getPlayer(Side.SENTE).intializeHand(List.of(validPiece.getClass()));
    
        // The initial state
        initialMoveCount = game.getMoveCount();
        initialTurn = game.getTurn();
        initialCapturedPieceCount = game.getPlayer(Side.GOTE).getHand().get(validPiece.getClass());
    
        // Play the hand
        game.getBoard().setAtPosition(new Pos(2,0), null); // Remove one piece from board
        game.playHand(validPos, validPiece);
    
        // Verify piece placement
        assertEquals(validPiece, game.getBoard().getPieceAt(validPos), "Piece should be placed on the board at the specified position.");
    
        // Verify turn change
        assertNotEquals(initialTurn, game.getTurn(), "Turn should change after a valid move.");
    
        // Verify move count increase
        assertEquals(initialMoveCount + 1, game.getMoveCount(), "Move count should increase by 1.");
    
        // Verify history update
        assertEquals(initialMoveCount, game.getHistory().getNumberOfMoves(), "History should have the same number of moves as the move count.");
    
        // Verify that the captured pieces of the player are updated
        assertEquals(initialCapturedPieceCount - 1, game.getPlayer(Side.GOTE).getHand().get(validPiece.getClass()), "Gote's captured pieces should be updated.");
        
        // Verify board change
        assertFalse(game.boardChangedProperty().get(), "Board should be marked as changed."); // boardChanged does !boardChangedProperty, meaning that it will return to false if we do it twice without handling

    }

    @Test
    void testGameFromSaveFile() {
        game = new Game(variant, 360);

        game.setCapturedPiecesFromSfen("pP2");



        SaveFile saveFile = new SaveFile(game);
        Game gameFromSavefile = new Game(saveFile); 
        gameFromSavefile.getClock(Side.SENTE).pause();
        assertEquals(gameFromSavefile.getHistory().getMoves(), game.getHistory().getMoves(), "History should be the same.");
        assertEquals(gameFromSavefile.getVariant().getClass(), game.getVariant().getClass(), "It should be the same Variant.");
        assertEquals(game.getPlayer(Side.SENTE).getHand(), gameFromSavefile.getPlayer(Side.SENTE).getHand(), "They should have the same hand.");
        assertEquals(game.getPlayer(Side.GOTE).getHand(), gameFromSavefile.getPlayer(Side.GOTE).getHand(), "They should have the same hand.");

        assertEquals(gameFromSavefile.getMoveCount(), game.getMoveCount(), "Move count should be the same.");
        assertEquals(gameFromSavefile.getTurn(), game.getTurn(), "The current turn should be the same.");

        if (gameFromSavefile.isClocksInitialized()) {
            assertEquals(gameFromSavefile.getTime(Side.SENTE), game.getTime(Side.SENTE), "Sente's time should be the same.");
            assertEquals(gameFromSavefile.getTime(Side.GOTE), game.getTime(Side.GOTE), "Gote's time should be the same.");
        }
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