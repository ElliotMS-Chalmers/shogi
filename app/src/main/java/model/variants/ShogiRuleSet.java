package model.variants;

import model.Board;
import model.pieces.King;
import model.pieces.Piece;
import util.Pos;
import util.Side;

import java.util.ArrayList;


public class ShogiRuleSet extends RuleSet{

    @Override
    public boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Side side, Side oppositeSide){
        if (piece instanceof King){
//            if (isCurrentlyInCheck(board, variant, posFrom, oppositeSide)){
//                if (isCurrentlyInCheck(board, variant, posTo, oppositeSide)){
//                    return false;
//                }
//            }
            return !isCurrentlyInCheck(board, posTo, oppositeSide) && piece.getAvailableMoves(posFrom, board).contains(posTo);
        }
        return (piece.getAvailableMoves(posFrom, board).contains(posTo));
    }

    public boolean isCurrentlyInCheck(Board board, Pos kingPos, Side oppositeSide){
        ArrayList<Pos> allPossibleMovesSente = new ArrayList<>();
        ArrayList<Pos> allPossibleMovesGote = new ArrayList<>();
        ArrayList<Piece> everyPiece = board.getEveryPiece();
        ArrayList<Pos> everyPiecePos = board.getEveryPiecePos();

        for (int i = 0; i < everyPiece.size(); i ++) {
            if (everyPiece.get(i).getSide() == Side.SENTE) {
                allPossibleMovesSente.addAll(everyPiece.get(i).getAvailableMoves(everyPiecePos.get(i), board));
            } else {
                allPossibleMovesGote.addAll(everyPiece.get(i).getAvailableMoves(everyPiecePos.get(i), board));
            }
        }

        if (oppositeSide == Side.SENTE){
            for (Pos possibleMoveSente: allPossibleMovesSente){
                if (possibleMoveSente.col() == kingPos.col() && possibleMoveSente.row() == kingPos.row()){
                    return true;
                }
            }
        } else {
            for (Pos possibleMoveGote: allPossibleMovesGote){
                if (possibleMoveGote.col() == kingPos.col() && possibleMoveGote.row() == kingPos.row()){
                    return true;
                }
            }
        }


        return false;
    }

    public boolean isCurrentlyInCheckMate(Board board, Pos kingPos, Side oppositeSide){
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

        for (int i = 0; i < everyPiece.size(); i ++) {
            if (everyPiece.get(i).getSide() == Side.SENTE) {
                everyPieceSente.add(everyPiece.get(i));
                everyPieceSentePos.add(everyPiecePos.get(i));
            } else {
                everyPieceGote.add(everyPiece.get(i));
                everyPieceGotePos.add(everyPiecePos.get(i));
            }
        }

        for (int i = 0; i <everyPiece.size(); i++){
            if (everyPiece.get(i).getAvailableMoves(everyPiecePos.get(i), board).contains(kingPos)){
                pieceForcingCheckMoves = everyPiece.get(i).getAvailableMoves(everyPiecePos.get(i), board);
                pieceForcingCheckMoves.add(everyPiecePos.get(i));
            }
        }
        // kan ta piece men kung även om den går in i schack
        // kan baka om det står ett torn framför
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
            for (int i = 0; i < everyPieceGote.size(); i ++){
                if (everyPieceGote.get(i).getClass() != King.class) {
                    assert pieceForcingCheckMoves != null;
                    for (Pos pieceForcingCheckMove : pieceForcingCheckMoves) {
                        if (everyPieceGote.get(i).getAvailableMoves(everyPieceGotePos.get(i), board).contains(pieceForcingCheckMove)) {
                            return false;
                        }
                    }
                }
            }
        }

        //Man kan även lägga ner en pjäs för att bloka

        return true;
    }

}
