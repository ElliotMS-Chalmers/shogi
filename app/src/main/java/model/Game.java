package model;

import model.pieces.*;
import model.variants.Variant;
import util.Pos;
import util.Side;

import java.lang.runtime.SwitchBootstraps;

public class Game {
    private boolean turn = false;
    private Variant variant;
    private Board board;
    private Player sentePlayer;
    private Player gotePlayer;
    private int moveCount = 1;
    private History history;

    public Game(Variant variant){
        this.variant = variant;

        this.board = new Board(variant.getWidth(), variant.getHeight());
        this.board.initializeBoard(variant.getStartSfen());

        this.history = new History();

        this.sentePlayer = new Player(Side.SENTE);
        this.sentePlayer.intializeHand(variant.getHand());

        this.gotePlayer = new Player(Side.GOTE);
        this.gotePlayer.intializeHand(variant.getHand());
    }
    public void move(Pos from, Pos to){
        if(board.getPieceAt(from) == null){return;}
        Move move = board.move(from, to);
        Piece capturedPiece = move.capturedPiece();
        if (capturedPiece != null) {
            switch (capturedPiece.getSide()) {
                case SENTE: gotePlayer.addCapturedPiece(move.capturedPiece().getClass()); break;
                case GOTE: sentePlayer.addCapturedPiece(move.capturedPiece().getClass()); break;
            }

        }
        changeTurn();
        moveCount++;
        history.addMove(move);
    }

    public Board getBoard() {
        return board;
    }

    public Variant getVariant() {
        return variant;
    }

    public Sfen getSfen() {
        System.out.println(new Sfen(board.getBoardAsSfen(), turn ? 'b' : 'w', getCapturedPiecesAsSfen(), moveCount));
        return new Sfen(board.getBoardAsSfen(), turn ? 'b' : 'w', getCapturedPiecesAsSfen(), moveCount);
    }

    public void undo(){
        Move lastMove = history.removeLast();
        changeTurn();
        moveCount--;
        if(lastMove.from() == null){
            //If lastMove.from is null then the move was a placement of a captured piece on the board
            //This undos that by returning the captured piece to the player's hand.
            (turn ? sentePlayer : gotePlayer).addCapturedPiece(lastMove.movedPiece().getClass());
        }else{
            board.move(lastMove.to(),lastMove.from()); //Plays the last move in reverse.
        }
        if(lastMove.capturedPiece() != null){
            (turn ? sentePlayer : gotePlayer).removeCapturedPiece(lastMove.capturedPiece().getClass());
            board.setAtPosition(lastMove.to(),lastMove.capturedPiece());
        }
    }

    public String getCapturedPiecesAsSfen(){
        String sfen = sentePlayer.getHandAsSfen() + gotePlayer.getHandAsSfen();
        return sfen.isEmpty() ? "-" : sfen;
    }

    public void setCapturedPiecesFromSfen(String sfen){
        int amount = 1;
        char c;
        for(int i = 0; i < sfen.length(); i++){
            c = sfen.charAt(i);
            if(Character.isDigit(c)){
                amount = (int) c;
                continue;
            }
            Class<? extends Piece> pieceClass = PieceFactory.fromSfenAbbreviation(String.valueOf(Character.toUpperCase(c))).getClass();
            if(Character.isUpperCase(c)){
                sentePlayer.addCapturedPiece(pieceClass,amount);
            }else{
                gotePlayer.addCapturedPiece(pieceClass,amount);
            }
            amount = 1;
        }
    }

    private void changeTurn(){
        if(turn){turn = false;}
        else{turn = true;}
    }

    public void playHand(Pos pos, Piece piece) {
        board.setAtPosition(pos, piece);
        switch (piece.getSide()) {
            case GOTE -> gotePlayer.removeCapturedPiece(piece.getClass());
            case SENTE -> sentePlayer.removeCapturedPiece(piece.getClass());
        }
    }
}
