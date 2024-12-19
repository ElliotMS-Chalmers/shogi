package model.variants;

import model.Board;
import model.Player;
import model.pieces.*;
import util.Pos;
import util.Side;

import java.util.ArrayList;


public class ShogiRuleSet implements RuleSet {

    @Override
    public boolean validMove(Pos posFrom, Pos posTo, Piece piece, Board board, Side side, Side oppositeSide) {
        if (piece instanceof King){
            if (isCurrentlyInCheck(board, posTo, oppositeSide)){return false;}
        }

        if (!piece.getAvailableMoves(posFrom, board).contains(posTo)) {return false;}

        if (checkIfNextMoveIsCheck(posFrom,posTo,board,side,oppositeSide)){return false;}

        return true;
    }

    public boolean validHandMove(Pos pos, Class pieceClass, Board board, Side side){
        if (board.getPieceAt(pos) != null){return false;}
        if (pieceClass == Pawn.class){
            if (board.getIfPieceInColum(side, Pawn.class, pos.col())){return false;}
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
    public boolean checkIfNextMoveIsCheck(Pos posFrom, Pos posTo, Board board, Side side, Side oppositeSide){
        Piece piece = board.testMove(posFrom, posTo, null);
        if (isCurrentlyInCheck(board, board.getPiecePos(side, King.class), oppositeSide)) {
            board.testMove(posTo, posFrom, piece);
            return true;
        }
        board.testMove(posTo, posFrom, piece);
        return false;
    }

    public boolean isCurrentlyInCheck(Board board, Pos kingPos, Side oppositeSide){
        ArrayList<Pos> allPossibleMovesSente = new ArrayList<>();
        ArrayList<Pos> allPossibleMovesGote = new ArrayList<>();
        ArrayList<Piece> everyPiece = board.getEveryPiece();
        ArrayList<Pos> everyPiecePos = board.getEveryPiecePos();

        for (int i = 0; i < everyPiece.size(); i ++) {
            if (everyPiece.get(i).getSide() == Side.SENTE) {
                allPossibleMovesSente.addAll(everyPiece.get(i).getAvailableMovesBackend(everyPiecePos.get(i), board));
            } else {
                allPossibleMovesGote.addAll(everyPiece.get(i).getAvailableMovesBackend(everyPiecePos.get(i), board));
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

    public boolean isCurrentlyInCheckMate(Board board, Pos kingPos, Side side, Side oppositeSide, Player player){
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
            if (everyPiece.get(i).getAvailableMovesBackend(everyPiecePos.get(i), board).contains(kingPos)){
                pieceForcingCheckMoves = everyPiece.get(i).getAvailableMoves(everyPiecePos.get(i), board);
                pieceForcingCheckMoves.add(everyPiecePos.get(i));
            }
        }
        if (oppositeSide == Side.GOTE && pieceForcingCheckMoves != null){
            for (int i = 0; i < everyPieceSente.size(); i ++){
                if (everyPieceSente.get(i).getClass() != King.class) {
                    for (Pos pieceForcingCheckMove : pieceForcingCheckMoves) {
                        if (everyPieceSente.get(i).getAvailableMovesBackend(everyPieceSentePos.get(i), board).contains(pieceForcingCheckMove)) {
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
                        if (everyPieceGote.get(i).getAvailableMovesBackend(everyPieceGotePos.get(i), board).contains(pieceForcingCheckMove)) {
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
                        if (validHandMove(new Pos(i,j), piece.getClass(), board, oppositeSide)){
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
