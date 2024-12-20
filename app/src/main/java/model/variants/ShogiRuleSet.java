package model.variants;

import model.Board;
import model.PieceFactory;
import model.Player;
import model.pieces.*;
import util.Pos;
import util.Side;

import java.util.ArrayList;

/**
 * The `ShogiRuleSet` class implements the `RuleSet` interface and provides the rule logic for the game of Shogi.
 * It contains methods for validating moves.
 */
public class ShogiRuleSet implements RuleSet {

    /**
     * Validates if a move is valid according to Shogi rules.
     * It checks if a move is legal, ensuring that a King is not moved into check and that the move does not result in a check for the player.
     *
     * @param posFrom the position of the piece being moved
     * @param posTo the target position to which the piece is moving
     * @param piece the piece being moved
     * @param board the game board
     * @param side the side of the player making the move (Sente or Gote)
     * @param oppositeSide the side of the opponent
     * @return true if the move is valid, false otherwise
     */
    @Override
    public boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Side side, Side oppositeSide) {
        if (piece instanceof King) {
            if (isCurrentlyInCheck(board, posTo, oppositeSide)) {
                return false;
            }
        }
        if (!piece.getAvailableMoves(posFrom, board).contains(posTo)) {
            return false;
        }
        if (checkIfNextMoveIsCheck(posFrom, posTo, board, side, oppositeSide)) {
            return false;
        }
        return true;
    }

    /**
     * Validates if a move made with a captured piece (from the hand) is legal.
     * This ensures that the piece is being placed on an empty square and adheres to the specific constraints for certain pieces (like Pawn, Knight, and Lance).
     *
     * @param pos the position to place the piece from the hand
     * @param pieceClass the class of the piece being moved from the hand
     * @param board the game board
     * @param side the side of the player making the move
     * @return true if the hand move is valid, false otherwise
     */
    public boolean validHandMove(Pos pos, Class pieceClass, Board board, Side side) {
        if (board.getPieceAt(pos) != null) {
            return false;
        }
        if (pieceClass == Pawn.class) {
            if (board.ifPieceInColum(side, Pawn.class, pos.col())) {
                return false;
            }
        }
        if (pieceClass == Knight.class) {
            if (side == Side.GOTE) {
                if (pos.row() < 2) {
                    return false;
                }
            } else {
                if (pos.row() > 6) {
                    return false;
                }
            }
        }
        if (pieceClass == Lance.class) {
            if (side == Side.GOTE) {
                if (pos.row() < 1) {
                    return false;
                }
            } else {
                if (pos.row() > 7) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the next move would place the player's King in check.
     * This method tests the move and ensures that after executing the move, the King is not in check.
     *
     * @param posFrom the current position of the piece being moved
     * @param posTo the target position to which the piece is moving
     * @param board the game board
     * @param side the side of the player making the move
     * @param oppositeSide the side of the opponent
     * @return true if the next move results in a check, false otherwise
     */
    public boolean checkIfNextMoveIsCheck(Pos posFrom, Pos posTo, Board board, Side side, Side oppositeSide) {
        Piece piece = board.testMove(posFrom, posTo, null);
        if (isCurrentlyInCheck(board, board.getPiecePos(side, King.class), oppositeSide)) {
            board.testMove(posTo, posFrom, piece);
            return true;
        }
        board.testMove(posTo, posFrom, piece);
        return false;
    }

    /**
     * Checks if the King's position is under attack (i.e., in check) by the opposite side.
     * It determines if any piece from the opponent has a move that can capture the King.
     *
     * @param board the game board
     * @param kingPos the position of the King's piece
     * @param oppositeSide the side of the opponent
     * @return true if the King is in check, false otherwise
     */
    public boolean isCurrentlyInCheck(Board board, Pos kingPos, Side oppositeSide) {
        ArrayList<Pos> allPossibleMovesSente = new ArrayList<>();
        ArrayList<Pos> allPossibleMovesGote = new ArrayList<>();
        ArrayList<Piece> everyPiece = board.getEveryPiece();
        ArrayList<Pos> everyPiecePos = board.getEveryPiecePos();

        for (int i = 0; i < everyPiece.size(); i++) {
            if (everyPiece.get(i).getSide() == Side.SENTE) {
                allPossibleMovesSente.addAll(everyPiece.get(i).getAvailableMovesBackend(everyPiecePos.get(i), board));
            } else {
                allPossibleMovesGote.addAll(everyPiece.get(i).getAvailableMovesBackend(everyPiecePos.get(i), board));
            }
        }

        if (oppositeSide == Side.SENTE) {
            for (Pos possibleMoveSente : allPossibleMovesSente) {
                if (possibleMoveSente.col() == kingPos.col() && possibleMoveSente.row() == kingPos.row()) {
                    return true;
                }
            }
        } else {
            for (Pos possibleMoveGote : allPossibleMovesGote) {
                if (possibleMoveGote.col() == kingPos.col() && possibleMoveGote.row() == kingPos.row()) {
                    return true;
                }
            }
        }


        return false;
    }

    /**
     * Determines if the player's King is in checkmate, which means the King is in check and cannot escape check.
     * It checks if any valid move exists to escape check and if all pieces cannot block or capture the attacking piece.
     *
     * @param board the game board
     * @param kingPos the position of the King
     * @param side the side of the player whose King is being checked
     * @param oppositeSide the side of the opponent
     * @param player the player whose King is under threat
     * @return true if the player is in checkmate, false otherwise
     */
    public boolean isCurrentlyInCheckMate(Board board, Pos kingPos, Side side, Side oppositeSide, Player player) {
        if (!isCurrentlyInCheck(board, kingPos, oppositeSide)){ return false;}

        for (Pos move : board.getPieceAt(kingPos).getAvailableMoves(kingPos, board)){
            if (!isCurrentlyInCheck(board, move, oppositeSide)){return false;}
        }

        ArrayList<Piece> everyPieceSente = new ArrayList<>();
        ArrayList<Piece> everyPieceGote = new ArrayList<>();
        ArrayList<Pos> everyPieceSentePos = new ArrayList<>();
        ArrayList<Pos> everyPieceGotePos = new ArrayList<>();
        ArrayList<Piece> everyPiece = board.getEveryPiece();
        ArrayList<Pos> everyPiecePos = board.getEveryPiecePos();
        ArrayList<Pos> pieceForcingCheckMoves = null;
        ArrayList<Pos> avaialableHandMoves = new ArrayList<>();

        for (int i = 0; i < everyPiece.size(); i++) {
            if (everyPiece.get(i).getSide() == Side.SENTE) {
                everyPieceSente.add(everyPiece.get(i));
                everyPieceSentePos.add(everyPiecePos.get(i));
            } else {
                everyPieceGote.add(everyPiece.get(i));
                everyPieceGotePos.add(everyPiecePos.get(i));
            }
        }

        for (int i = 0; i <everyPiece.size(); i++){
            if (everyPiece.get(i).getSide() == side.opposite()) {
                if (everyPiece.get(i).getAvailableMovesBackend(everyPiecePos.get(i), board).contains(kingPos)) {
                    pieceForcingCheckMoves = everyPiece.get(i).getForcingCheckMoves(everyPiecePos.get(i), kingPos, board);
                    pieceForcingCheckMoves.add(everyPiecePos.get(i));

                }
            }
        }

        if (oppositeSide == Side.GOTE && pieceForcingCheckMoves != null){
            for (int i = 0; i < everyPieceSente.size(); i ++){
                if (everyPieceSente.get(i).getClass() != King.class) {
                    for (Pos pieceForcingCheckMove : pieceForcingCheckMoves) {
                        if (everyPieceSente.get(i).getAvailableMoves(everyPieceSentePos.get(i), board).contains(pieceForcingCheckMove)) {
                            return false;
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < everyPieceGote.size(); i++) {
                if (everyPieceGote.get(i).getClass() != King.class) {
                    for (Pos pieceForcingCheckMove : pieceForcingCheckMoves) {
                        if (everyPieceGote.get(i).getAvailableMoves(everyPieceGotePos.get(i), board).contains(pieceForcingCheckMove)) {
                            return false;
                        }
                    }
                }
            }
        }

        for (Class<? extends Piece> piece : player.getHand().keySet()){
            if (player.getHand().get(piece) > 0){
                for (int i = 0; i < board.getWidth(); i++) {
                    for (int j = 0; j < board.getHeight(); j++) {
                        if (validHandMove(new Pos(i,j), piece, board, oppositeSide)){
                            avaialableHandMoves.add(new Pos(i,j));
                        }
                    }
                }
            }

        }

        //bug med att man kan gå bakom kung med en annan pejäs för att stoppa schack matt
        if (pieceForcingCheckMoves != null) {
            for (Pos pieceForcingCheckMove : pieceForcingCheckMoves){
                if (avaialableHandMoves.contains(pieceForcingCheckMove)){
                    return false;
                }
            }
        }
        return true;
    }
}
