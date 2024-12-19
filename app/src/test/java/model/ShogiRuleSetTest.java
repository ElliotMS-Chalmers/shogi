package model;

import model.variants.*;
import model.Board;
import model.pieces.King;
import model.pieces.Pawn;
import model.pieces.Rook;
import org.junit.jupiter.api.Test;
import util.Pos;
import util.Side;

import static org.junit.jupiter.api.Assertions.*;

class ShogiRuleSetTest {
    @Test
    void testValidMove_KingNotInCheck() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place a king on the board
        King king = new King(Side.SENTE);
        Pos from = new Pos(4, 4); // King's starting position
        board.setAtPosition(from, king);

        // Move the king to a safe position
        Pos to = new Pos(4, 5);

        // Create an instance of ShogiRuleSet and check if the move is valid
        ShogiRuleSet ruleSet = new ShogiRuleSet();
        boolean validMove = ruleSet.validMove(from, to, king, board, Side.SENTE, Side.GOTE);

        // We expect the move to be valid
        assertTrue(validMove);
    }

    @Test
    void testValidMove_KingInCheck() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place the king and an enemy piece (e.g., Rook) that threatens the king
        King king = new King(Side.SENTE);
        board.setAtPosition(new Pos(4, 4), king);
        Rook rook = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(4, 7), rook);

        // Attempt to move the king to a position that is still in check
        Pos from = new Pos(4, 4);
        Pos to = new Pos(4, 5);

        ShogiRuleSet ruleSet = new ShogiRuleSet();
        boolean validMove = ruleSet.validMove(from, to, king, board, Side.SENTE, Side.GOTE);

        // We expect the move to be invalid
        assertFalse(validMove);
    }

    @Test
    void testValidHandMove_Pawn() {
        // Initialize variant and board
        Variant miniVariant = new Mini();
        Board board = new Board(miniVariant.getWidth(), miniVariant.getHeight());

        // Create a pawn and place it on the hand
        Pawn pawn = new Pawn(Side.SENTE);

        // Attempt to place the pawn on an empty position
        Pos position = new Pos(3, 3);

        ShogiRuleSet ruleSet = new ShogiRuleSet();
        boolean validHandMove = ruleSet.validHandMove(position, pawn.getClass(), board, Side.SENTE);

        // We expect the hand move to be valid
        assertTrue(validHandMove);
    }

    @Test
    void testInvalidHandMove_PawnInColumn() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place another piece in the same column where we try to place a Pawn
        Pawn pawn = new Pawn(Side.GOTE);
        board.setAtPosition(new Pos(3, 3), pawn);

        // Attempt to place a pawn (Pawn) in the same column
        Pawn newPawn = new Pawn(Side.SENTE);
        Pos position = new Pos(3, 3);

        ShogiRuleSet ruleSet = new ShogiRuleSet();
        boolean validHandMove = ruleSet.validHandMove(position, newPawn.getClass(), board, Side.SENTE);

        // We expect the move to be invalid, as a pawn cannot be placed in a column where there is already a pawn
        assertFalse(validHandMove);
    }

    @Test
    void testIsCurrentlyInCheck_SenteKingInCheck() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place a king and an enemy Rook that threatens the king
        King senteKing = new King(Side.SENTE);
        board.setAtPosition(new Pos(4, 4), senteKing);
        Rook goteRook = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(4, 7), goteRook);

        ShogiRuleSet ruleSet = new ShogiRuleSet();

        // Check if the king is in check
        boolean isInCheck = ruleSet.isCurrentlyInCheck(board, new Pos(4, 4), Side.GOTE);

        // We expect the king to be in check
        assertTrue(isInCheck);
    }

    @Test
    void testIsCurrentlyInCheck_NoCheck() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place the king and an enemy Rook that does not threaten the king
        King senteKing = new King(Side.SENTE);
        board.setAtPosition(new Pos(4, 4), senteKing);
        Rook goteRook = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(5, 5), goteRook);

        ShogiRuleSet ruleSet = new ShogiRuleSet();

        // Check that the king is not in check
        boolean isInCheck = ruleSet.isCurrentlyInCheck(board, new Pos(4, 4), Side.GOTE);

        // We expect the king not to be in check
        assertFalse(isInCheck);
    }

    @Test
    void testIsCurrentlyInCheckMate() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());
        Player player = new Player(Side.SENTE);

        // Place the king in a checkmate situation
        King senteKing = new King(Side.SENTE);
        board.setAtPosition(new Pos(4, 4), senteKing);
        Rook goteRook = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(4, 7), goteRook);
        Rook goteRook2 = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(3, 7), goteRook2);
        Rook goteRook3 = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(5, 7), goteRook3);

        ShogiRuleSet ruleSet = new ShogiRuleSet();

        // Check if it's checkmate
        boolean isInCheckMate = ruleSet.isCurrentlyInCheckMate(board,new Pos(4, 4), Side.SENTE,Side.GOTE,player);

        // We expect it to be checkmate
        assertTrue(isInCheckMate);
    }

}
