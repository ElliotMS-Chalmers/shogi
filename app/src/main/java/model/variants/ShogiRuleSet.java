package model.variants;

import model.Board;
import model.Player;
import model.pieces.*;
import util.Pos;
import util.Side;

import java.util.ArrayList;


public class ShogiRuleSet implements RuleSet {

    @Override
    public boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Variant variant, Side side, Side oppositeSide) {
        if (piece instanceof King){
            if (isCurrentlyInCheck(board, variant, posTo, oppositeSide)){return false;}
        }

        if (!piece.getAvailableMoves(posFrom, board, variant).contains(posTo)) {return false;}

        if (checkIfNextMoveIsCheck(posFrom,posTo,board,variant,side,oppositeSide)){return false;}

        return true;
    }

    public boolean validHandMove(Pos pos, Class pieceClass, Board board, Variant variant, Side side){
        if (board.getPieceAt(pos) != null){return false;}
        if (pieceClass == Pawn.class){
            if (board.getIfPieceInColum(variant, side, Pawn.class, pos.col())){return false;}
        }
        if (pieceClass == Knight.class){
            if (side == Side.GOTE){
                if (pos.row() < 2){return false;}
            } else {
                if (pos.row() > 6){return false;}
            }
        }
        if (pieceClass == Lance.class){
            if (side == Side.GOTE){
                if (pos.row() < 1){return false;}
            } else {
                if (pos.row() > 7){return false;}
            }
        }


        return true;
    }
    public boolean checkIfNextMoveIsCheck(Pos posFrom, Pos posTo, Board board, Variant variant, Side side, Side oppositeSide){
        Piece piece = board.testMove(posFrom, posTo, null);
        if (isCurrentlyInCheck(board, variant, board.getPiecePos(variant, side, King.class), oppositeSide)) {
            board.testMove(posTo, posFrom, piece);
            return true;
        }
        board.testMove(posTo, posFrom, piece);
        return false;
    }

    public boolean isCurrentlyInCheck(Board board, Variant variant, Pos kingPos, Side oppositeSide){
        ArrayList<Pos> allPossibleMovesSente = new ArrayList<>();
        ArrayList<Pos> allPossibleMovesGote = new ArrayList<>();
        ArrayList<Piece> everyPiece = board.getEveryPiece(variant);
        ArrayList<Pos> everyPiecePos = board.getEveryPiecePos(variant);

        for (int i = 0; i < everyPiece.size(); i ++) {
            if (everyPiece.get(i).getSide() == Side.SENTE) {
                allPossibleMovesSente.addAll(everyPiece.get(i).getAvailableMovesBackend(everyPiecePos.get(i), board, variant));
            } else {
                allPossibleMovesGote.addAll(everyPiece.get(i).getAvailableMovesBackend(everyPiecePos.get(i), board, variant));
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

    public boolean isCurrentlyInCheckMate(Board board, Variant variant, Pos kingPos, Side side, Side oppositeSide, Player player){
        if (!isCurrentlyInCheck(board, variant, kingPos, oppositeSide)){ return false;}
        for (Pos move : board.getPieceAt(kingPos).getAvailableMoves(kingPos, board, variant)){
            if (!isCurrentlyInCheck(board, variant, move, oppositeSide)){return false;}
        }
        ArrayList<Piece> everyPieceSente = new ArrayList<>();
        ArrayList<Piece> everyPieceGote = new ArrayList<>();
        ArrayList<Pos> everyPieceSentePos = new ArrayList<>();
        ArrayList<Pos> everyPieceGotePos = new ArrayList<>();
        ArrayList<Piece> everyPiece = board.getEveryPiece(variant);
        ArrayList<Pos> everyPiecePos = board.getEveryPiecePos(variant);
        ArrayList<Pos> pieceForcingCheckMoves = null;

        ArrayList<Pos> avaialableHandMoves = new ArrayList<>();


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
            if (everyPiece.get(i).getAvailableMovesBackend(everyPiecePos.get(i), board, variant).contains(kingPos)){
                pieceForcingCheckMoves = everyPiece.get(i).getAvailableMoves(everyPiecePos.get(i), board, variant);
                pieceForcingCheckMoves.add(everyPiecePos.get(i));
            }
        }
        if (oppositeSide == Side.GOTE && pieceForcingCheckMoves != null){
            for (int i = 0; i < everyPieceSente.size(); i ++){
                if (everyPieceSente.get(i).getClass() != King.class) {
                    for (Pos pieceForcingCheckMove : pieceForcingCheckMoves) {
                        if (everyPieceSente.get(i).getAvailableMovesBackend(everyPieceSentePos.get(i), board, variant).contains(pieceForcingCheckMove)) {
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
                        if (everyPieceGote.get(i).getAvailableMovesBackend(everyPieceGotePos.get(i), board, variant).contains(pieceForcingCheckMove)) {
                            return false;
                        }
                    }
                }
            }
        }


        for (Class<? extends Piece> piece : player.getHand().keySet()){
            if (player.getHand().get(piece) > 0){
                for (int i = 0; i < variant.getWidth(); i++) {
                    for (int j = 0; j < variant.getHeight(); j++) {
                        if (validHandMove(new Pos(i,j), piece.getClass(), board, variant, oppositeSide)){
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
