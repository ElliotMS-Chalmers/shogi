package model;

import model.pieces.*;
import model.variants.Variant;
import util.Pos;
import util.Sfen;
import util.Side;

public class Game {
    private boolean turn = false;
    private Variant variant;
    private Board board;
    private Player sentePlayer = new Player(new Piece[] {}, new Piece[] { new Pawn(Side.SENTE), new Knight(Side.SENTE), new Lance(Side.SENTE) }, false); // TESTING
    private Player gotePlayer = new Player(new Piece[] {}, new Piece[] { new Pawn(Side.GOTE), new Knight(Side.GOTE), new Lance(Side.GOTE), new Lance(Side.GOTE) }, true); // TESTING
    private int moveCount = 1;
    private History history;

    public Game(Variant variant){
        this.variant = variant;
        this.board = new Board(variant.getWidth(), variant.getHeight());
        this.board.initializeBoard(variant.getStartSfen());
        this.history = new History(variant.getStartSfen());
    }
    public void move(Pos from, Pos to){
        Piece piece = board.getPieceAt(from);
        if(piece == null){return;}
        this.board.move(from, to);
        changeTurn();
        moveCount++;
        history.addMove(from,to,piece,turn,board.getBoardAsSfen(), getCapturedPiecesAsSfen());
    }

    public Board getBoard() {
        return board;
    }

    public Sfen getSfen() {
        return new Sfen(board.getBoardAsSfen(), turn ? 'b' : 'w', sentePlayer.getHandAsSfen() + gotePlayer.getHandAsSfen(), moveCount);
    }

    public void undo(){
        board.setSfen(history.undo());
        changeTurn();
        moveCount--;
    }

    public String getCapturedPiecesAsSfen(){
        return sentePlayer.getHandAsSfen() + gotePlayer.getHandAsSfen().toLowerCase();
    }

    private void changeTurn(){
        if(turn){turn = false;}
        else{turn = true;}
    }
}
