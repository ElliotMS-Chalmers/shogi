package model.variants;

import model.game.Player;
import model.game.Board;
import model.pieces.King;
import model.pieces.*;
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

    @Test
    void testValidHandMove_Knight() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Create a knight and try to place it on an allowed position
        Knight knight = new Knight(Side.SENTE);
        Pos position = new Pos(6, 4); // A valid position for SENTE

        ShogiRuleSet ruleSet = new ShogiRuleSet();
        boolean validHandMove = ruleSet.validHandMove(position, knight.getClass(), board, Side.SENTE);

        // We expect the hand move to be valid for SENTE
        assertTrue(validHandMove, "Knight should be placed in valid position for SENTE");
    }

    @Test
    void testInvalidHandMove_Knight_ForGote() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Create a knight and try to place it on an invalid position for GOTE
        Knight knight = new Knight(Side.GOTE);
        Pos position = new Pos(1, 1); // Invalid row for GOTE (should be >= 2)

        ShogiRuleSet ruleSet = new ShogiRuleSet();
        boolean validHandMove = ruleSet.validHandMove(position, knight.getClass(), board, Side.GOTE);

        // We expect the hand move to be invalid for GOTE
        assertFalse(validHandMove, "Knight cannot be placed on row < 2 for GOTE");
    }

    @Test
    void testValidHandMove_Lance() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Create a lance and try to place it on an allowed position for SENTE
        Lance lance = new Lance(Side.SENTE);
        Pos position = new Pos(7, 4); // A valid position for SENTE

        ShogiRuleSet ruleSet = new ShogiRuleSet();
        boolean validHandMove = ruleSet.validHandMove(position, lance.getClass(), board, Side.SENTE);

        // We expect the hand move to be valid for SENTE
        assertTrue(validHandMove, "Lance should be placed in valid position for SENTE");
    }

    @Test
    void testInvalidHandMove_Lance_ForGote() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Create a lance and try to place it on an invalid position for GOTE
        Lance lance = new Lance(Side.GOTE);
        Pos position = new Pos(0, 4); // Invalid row for GOTE (should be >= 1)

        ShogiRuleSet ruleSet = new ShogiRuleSet();
        boolean validHandMove = ruleSet.validHandMove(position, lance.getClass(), board, Side.GOTE);

        // We expect the hand move to be invalid for GOTE
        assertFalse(validHandMove, "Lance cannot be placed on row < 1 for GOTE");
    }

    @Test
    void testInvalidHandMove_Pawn_InColumn() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place a pawn in the same column where we try to place another pawn
        Pawn pawn1 = new Pawn(Side.SENTE);
        board.setAtPosition(new Pos(3, 3), pawn1);

        // Attempt to place another pawn in the same column
        Pawn pawn2 = new Pawn(Side.GOTE);
        Pos position = new Pos(3, 3); // Same column as pawn1

        ShogiRuleSet ruleSet = new ShogiRuleSet();
        boolean validHandMove = ruleSet.validHandMove(position, pawn2.getClass(), board, Side.GOTE);

        // We expect the move to be invalid because a pawn cannot be placed in the same column
        assertFalse(validHandMove, "Pawn cannot be placed in a column where there is already a pawn");
    }

    @Test
    void testKingMoveIntoCheck() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place a SENTE King on a starting position
        King senteKing = new King(Side.SENTE);
        Pos kingPosition = new Pos(4, 4); // King's starting position
        board.setAtPosition(kingPosition, senteKing);

        // Place an enemy GOTE Rook on a position threatening the King
        Rook goteRook = new Rook(Side.GOTE);
        Pos rookPosition = new Pos(4, 7); // Rook threatens King's position on the same row
        board.setAtPosition(rookPosition, goteRook);

        // Attempt to move the King to a position that is under attack
        Pos newKingPosition = new Pos(4, 5); // King tries to move to a threatened square (same row as Rook)

        ShogiRuleSet ruleSet = new ShogiRuleSet();

        // Check if the King would end up in check
        boolean isInCheck = ruleSet.isCurrentlyInCheck(board, newKingPosition, Side.SENTE);

        // Expect the King to be in check if it moves to the threatened square
        assertTrue(isInCheck, "King should be in check after moving to the attacked square");
    }

    @Test
    void testKingCanMoveOutOfCheck() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place SENTE's King on a position
        King senteKing = new King(Side.SENTE);
        Pos senteKingPos = new Pos(4, 4);
        board.setAtPosition(senteKingPos, senteKing);

        // Place a GOTE Rook threatening the King
        Rook goteRook = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(4, 7), goteRook);

        ShogiRuleSet ruleSet = new ShogiRuleSet();

        // Expect the King not to be in checkmate, as it can move to a different position
        boolean isInCheckMate = ruleSet.isCurrentlyInCheckMate(board, senteKingPos, Side.SENTE, Side.GOTE, new Player(Side.SENTE));
        assertFalse(isInCheckMate, "King should be able to move out of check and avoid checkmate.");
    }

    // Test: King is in check but cannot move
    @Test
    void testKingCannotMoveOutOfCheck() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place SENTE's King on a position
        King senteKing = new King(Side.SENTE);
        Pos senteKingPos = new Pos(4, 4);
        board.setAtPosition(senteKingPos, senteKing);

        // Place a GOTE Rook threatening the King on the same row
        Rook goteRook = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(4, 7), goteRook);

        // Place SENTE Pawns blocking the King's movement
        Pawn sentePawn1 = new Pawn(Side.SENTE);
        board.setAtPosition(new Pos(4, 3), sentePawn1);

        Pawn sentePawn2 = new Pawn(Side.SENTE);
        board.setAtPosition(new Pos(3, 4), sentePawn2);

        Pawn sentePawn3 = new Pawn(Side.SENTE);
        board.setAtPosition(new Pos(5, 4), sentePawn3);

        Pawn sentePawn4 = new Pawn(Side.SENTE);
        board.setAtPosition(new Pos(3, 5), sentePawn4);

        Knight senteKnight = new Knight(Side.SENTE);
        board.setAtPosition(new Pos(5, 5), senteKnight);

        Pawn sentePawn6 = new Pawn(Side.SENTE);
        board.setAtPosition(new Pos(3, 3), sentePawn6);

        Pawn sentePawn7 = new Pawn(Side.SENTE);
        board.setAtPosition(new Pos(5, 3), sentePawn7);

        ShogiRuleSet ruleSet = new ShogiRuleSet();

        // Expect the King to be in checkmate, as it cannot move anywhere without being in check
        boolean isInCheckMate = ruleSet.isCurrentlyInCheckMate(board, senteKingPos, Side.SENTE, Side.GOTE, new Player(Side.SENTE));
        assertTrue(isInCheckMate, "King should be in checkmate as it cannot move out of check.");
    }


    @Test
    void testKingCannotMoveOutOfCheck_SenteAttacks() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place GOTE's king at a position
        King goteKing = new King(Side.GOTE);
        Pos goteKingPos = new Pos(4, 4);
        board.setAtPosition(goteKingPos, goteKing);

        // Place a SENTE Rook threatening the king on the same row
        Rook senteRook = new Rook(Side.SENTE);
        board.setAtPosition(new Pos(4, 7), senteRook);

        // Place a Pawn for GOTE blocking the king from moving
        Pawn gotePawn1 = new Pawn(Side.GOTE);
        board.setAtPosition(new Pos(4, 3), gotePawn1);

        Pawn gotePawn2 = new Pawn(Side.GOTE);
        board.setAtPosition(new Pos(3, 4), gotePawn2);

        Pawn gotePawn3 = new Pawn(Side.GOTE);
        board.setAtPosition(new Pos(5, 4), gotePawn3);

        Knight goteKnigt = new Knight(Side.GOTE);
        board.setAtPosition(new Pos(3, 5), goteKnigt);

        Pawn gotePawn5 = new Pawn(Side.GOTE);
        board.setAtPosition(new Pos(5, 5), gotePawn5);

        Pawn gotePawn6 = new Pawn(Side.GOTE);
        board.setAtPosition(new Pos(3, 3), gotePawn6);

        Pawn gotePawn7 = new Pawn(Side.GOTE);
        board.setAtPosition(new Pos(5, 3), gotePawn7);

        ShogiRuleSet ruleSet = new ShogiRuleSet();

        // Expect the king to be in checkmate, as it cannot move anywhere without being in check
        boolean isInCheckMate = ruleSet.isCurrentlyInCheckMate(board, goteKingPos, Side.GOTE, Side.SENTE, new Player(Side.GOTE));
        assertTrue(isInCheckMate, "Gote king should be in checkmate as it cannot move out of check.");
    }

    // Test: The king is in check, but another SENTE piece can block to stop the check
    @Test
    void testPieceCanBlockCheck() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place SENTE's king at a position
        King senteKing = new King(Side.SENTE);
        Pos senteKingPos = new Pos(4, 4);
        board.setAtPosition(senteKingPos, senteKing);

        // Place a GOTE Rook threatening the king
        Rook goteRook = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(4, 7), goteRook);

        // Place a SENTE Pawn that can block the Rook's path
        Pawn sentePawn = new Pawn(Side.SENTE);
        board.setAtPosition(new Pos(4, 6), sentePawn);

        ShogiRuleSet ruleSet = new ShogiRuleSet();

        // Expect the king not to be in checkmate, as SENTE's Pawn can block the Rook's move
        boolean isInCheckMate = ruleSet.isCurrentlyInCheckMate(board, senteKingPos, Side.SENTE, Side.GOTE, new Player(Side.SENTE));
        assertFalse(isInCheckMate, "King should not be in checkmate, as a piece can block the check.");
    }

    // Test: The king is in check and there are no possible moves to block or save it (checkmate)
    @Test
    void testNoPieceCanBlockCheck() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place SENTE's king at a position
        King senteKing = new King(Side.SENTE);
        Pos senteKingPos = new Pos(4, 4);
        board.setAtPosition(senteKingPos, senteKing);

        // Place a GOTE Rook threatening the king
        Rook goteRook = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(4, 7), goteRook);

        // Place another GOTE Rook blocking all possible blocks for SENTE
        Rook goteRook2 = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(3, 7), goteRook2);

        // Place another GOTE Rook on the same row to completely enclose the king
        Rook goteRook3 = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(5, 7), goteRook3);

        ShogiRuleSet ruleSet = new ShogiRuleSet();

        // Expect the king to be in checkmate, as no piece can block the check move
        boolean isInCheckMate = ruleSet.isCurrentlyInCheckMate(board, senteKingPos, Side.SENTE, Side.GOTE, new Player(Side.SENTE));
        assertTrue(isInCheckMate, "King should be in checkmate, as no piece can block the check.");
    }

    // Test: The king is not in check, and we expect it not to be in checkmate
    @Test
    void testKingNotInCheckMate() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place SENTE's king at a position
        King senteKing = new King(Side.SENTE);
        Pos senteKingPos = new Pos(4, 4);
        board.setAtPosition(senteKingPos, senteKing);

        // Place a GOTE Rook at another position not threatening the king
        Rook goteRook = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(4, 6), goteRook);

        ShogiRuleSet ruleSet = new ShogiRuleSet();

        // Expect the king not to be in checkmate, as there is no threat against it
        boolean isInCheckMate = ruleSet.isCurrentlyInCheckMate(board, senteKingPos, Side.SENTE, Side.GOTE, new Player(Side.SENTE));
        assertFalse(isInCheckMate, "King should not be in checkmate, as it's not in check.");
    }

    @Test
    void testCheckIfNextMoveIsCheck_CausingCheck() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place SENTE's king at a position
        King senteKing = new King(Side.SENTE);
        Pos senteKingPos = new Pos(4, 4);
        board.setAtPosition(senteKingPos, senteKing);

        // Place a GOTE Rook threatening the king on the same row
        Rook goteRook = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(4, 7), goteRook);

        // Place a SENTE Pawn at a position to make a move
        Pawn sentePawn = new Pawn(Side.SENTE);
        Pos sentePawnPos = new Pos(4, 5);
        board.setAtPosition(sentePawnPos, sentePawn);

        // Try to move SENTE Pawn to (4, 4), which will cause the king to be in check
        ShogiRuleSet ruleSet = new ShogiRuleSet();
        boolean causesCheck = ruleSet.checkIfNextMoveIsCheck(sentePawnPos, new Pos(5, 5), board, Side.SENTE, Side.GOTE);

        // Expect the method to return true as the move causes a check
        assertTrue(causesCheck, "Move should cause check as Rook is threatening the King.");
    }

    @Test
    void testCheckIfNextMoveIsCheck_NoCheck() {
        // Initialize variant and board
        Variant standardVariant = new Standard();
        Board board = new Board(standardVariant.getWidth(), standardVariant.getHeight());

        // Place SENTE's king at a position
        King senteKing = new King(Side.SENTE);
        Pos senteKingPos = new Pos(4, 4);
        board.setAtPosition(senteKingPos, senteKing);

        // Place a GOTE Rook at another position not threatening the king
        Rook goteRook = new Rook(Side.GOTE);
        board.setAtPosition(new Pos(1, 4), goteRook);

        // Place a SENTE Pawn at a position to make a move
        Pawn sentePawn = new Pawn(Side.SENTE);
        Pos sentePawnPos = new Pos(3, 4);
        board.setAtPosition(sentePawnPos, sentePawn);

        // Try to move SENTE Pawn to another position that doesn't cause check
        ShogiRuleSet ruleSet = new ShogiRuleSet();
        boolean causesCheck = ruleSet.checkIfNextMoveIsCheck(sentePawnPos, new Pos(2, 4), board, Side.SENTE, Side.GOTE);

        // Expect the method to return false as the move does not cause a check
        assertFalse(causesCheck, "Move should not cause check as Rook is not threatening the King.");
    }
}
