package model;

import model.pieces.*;
import model.variants.RuleSet;
import model.variants.Variant;
import util.Pos;
import util.Side;

import java.util.concurrent.atomic.AtomicBoolean;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Game {
    // private boolean turn = false;
    private Side turn = Side.SENTE;
    private Side oppositeTurn = Side.GOTE;
    private Variant variant;
    private Board board;
    private BooleanProperty boardChanged = new SimpleBooleanProperty(false);
    private Player sentePlayer;
    private Player gotePlayer;
    private Clock senteClock, goteClock;
    private Thread senteth, goteth;
    private AtomicBoolean gameRunning = new AtomicBoolean(true);
    private int moveCount = 1;
    private History history;
    private RuleSet ruleSet;

    public Game(Variant variant, Integer time){
        this.variant = variant;
        this.ruleSet = variant.getRuleSet();

        this.board = new Board(variant.getWidth(), variant.getHeight());
        this.board.initializeBoard(variant.getStartSfen());

        this.history = new History();

        this.sentePlayer = new Player(Side.SENTE);
        this.sentePlayer.intializeHand(variant.getHand());

        this.gotePlayer = new Player(Side.GOTE);
        this.gotePlayer.intializeHand(variant.getHand());

        if (time != 0) {
            setClocks(time);
            startClocks();
        }

        shutdownHook();
    }

    public Game(SaveFile saveFile){
        this.variant = saveFile.getVariant();
        this.ruleSet = variant.getRuleSet();

        this.board = new Board(variant.getWidth(), variant.getHeight());
        this.board.initializeBoard(saveFile.getSfen());

        this.history = saveFile.getHistory();

        this.sentePlayer = new Player(Side.SENTE);
        this.sentePlayer.intializeHand(variant.getHand());

        this.gotePlayer = new Player(Side.GOTE);
        this.gotePlayer.intializeHand(variant.getHand());

        this.moveCount = saveFile.getSfen().getMoveCount();
        this.turn = saveFile.getSfen().getTurn() == 'b' ? Side.SENTE : Side.GOTE;

        int senteTime = saveFile.getTime(Side.SENTE);
        int goteTime = saveFile.getTime(Side.GOTE);
        setClock(Side.SENTE, senteTime);
        setClock(Side.GOTE, goteTime);
        startClocks();

        shutdownHook();
    }

    public Move move(Pos from, Pos to){
        if (board.getPieceAt(from) == null || !ruleSet.validMove(from, to, board.getPieceAt(from), board, variant, turn, oppositeTurn)) { return null; }
        Move move = board.move(from, to);
        Piece capturedPiece = move.capturedPiece();
        if (capturedPiece != null) {
            switch (capturedPiece.getSide()) {
                case SENTE:
                    gotePlayer.addCapturedPiece(move.capturedPiece().getClass());
                    break;
                case GOTE:
                    sentePlayer.addCapturedPiece(move.capturedPiece().getClass());
                    break;
            }
        }
        changeTurn();
        moveCount++;
        history.addMove(move);
        boardChanged();
        return move;
    }

    private void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (gameRunning != null && gameRunning.get()) {
                stopClock();  // Stops the clocks
                // Ensure that clock threads finish before shutdown
                try {
                    if (senteth != null && senteth.isAlive()) {
                        senteth.interrupt();
                        senteth.join(); // Wait for the thread to finish
                    }
                    if (goteth != null && goteth.isAlive()) {
                        goteth.interrupt();
                        goteth.join(); // Wait for the thread to finish
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // Restore the interrupted status
                }
            }
        }));
    }

    public IntegerProperty timeProperty(Side side) {
        IntegerProperty time = null;
        if (isClocksInitialized()) {
            switch (side) {
                case SENTE -> time = senteClock.getSeconds();
                case GOTE -> time = goteClock.getSeconds();
            }
        }
        return time;
    }

    public Integer getTime(Side side) {
        IntegerProperty time = null;
        if (isClocksInitialized()) {
            switch (side) {
                case SENTE -> time = senteClock.getSeconds();
                case GOTE -> time = goteClock.getSeconds();
            }
        }
        if (time == null) return 0;
        return time.get();
    }

    public boolean isClocksInitialized() {
        return (senteClock != null && goteClock != null);
    }
    public AtomicBoolean getGameRunning() {
        return gameRunning;
    }

    public Board getBoard() {
        return board;
    }

    public History getHistory() {return history;}

    public BooleanProperty boardChangedProperty() {
        return boardChanged;
    }

    public Variant getVariant() {
        return variant;
    }

    public Sfen getSfen() {
        //System.out.println(new Sfen(board.getBoardAsSfen(), turn ? 'b' : 'w', getCapturedPiecesAsSfen(), moveCount));
        return new Sfen(board.getBoardAsSfen(), turn == Side.SENTE ? 'b' : 'w', getCapturedPiecesAsSfen(), moveCount);
    }

    //CLock
    public void setClocks(int seconds) {
        if (seconds != 0){
            // gameRunning.set(true); moved to start clocks
            this.senteClock = new Clock(seconds, Side.SENTE, gameRunning);
            this.goteClock = new Clock(seconds, Side.GOTE, gameRunning);
        }
    }

    public void setClock(Side side, int seconds) {
        if (seconds != 0){
            switch (side) {
                case SENTE: this.senteClock = new Clock(seconds, Side.SENTE, gameRunning); break;
                case GOTE: this.goteClock = new Clock(seconds, Side.GOTE, gameRunning); break;
            }
        }
    }

    public void startClocks(){
        gameRunning.set(true);
        senteth = new Thread(this.senteClock);
        goteth = new Thread(this.goteClock);
        senteth.start();
        goteth.start();
        switch (turn) {
            case SENTE -> goteClock.pause();
            case GOTE -> senteClock.pause();
        }
    }

    public void stopClock() {
        synchronized (gameRunning) {
            gameRunning.set(false);
            if (senteth != null && senteth.isAlive()) {
                senteth.interrupt();
            }
            if (goteth != null && goteth.isAlive()) {
                goteth.interrupt();
            }
            System.out.println("Clocks stopped.");
        }
    }

    public void undo(){
        Move lastMove = history.removeLast();
        changeTurn();
        moveCount--;
        if(lastMove.fromPlayerHand()){
            (turn == Side.SENTE ? sentePlayer : gotePlayer).addCapturedPiece(lastMove.movedPiece().getClass());
        }else{
            board.move(lastMove.to(),lastMove.from()); //Plays the last move in reverse.
        }
        if(lastMove.capturedPiece() != null){
            (turn == Side.SENTE ? sentePlayer : gotePlayer).removeCapturedPiece(lastMove.capturedPiece().getClass());
        }
        board.setAtPosition(lastMove.to(),lastMove.capturedPiece());
        boardChanged();
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
        turn = switch(turn) {
            case SENTE -> Side.GOTE;
            case GOTE -> Side.SENTE;
        };
        oppositeTurn = switch(oppositeTurn) {
            case SENTE -> Side.GOTE;
            case GOTE -> Side.SENTE;
        };
        if (senteClock != null && goteClock != null)
            changeActiveClock();
    }

    private void changeActiveClock() {
        switch (turn) {
            case SENTE -> { senteClock.resume(); goteClock.pause();}
            case GOTE -> { goteClock.resume(); senteClock.pause();}
        }
    }

    public void playHand(Pos pos, Piece piece) {
        board.setAtPosition(pos, piece);
        switch (piece.getSide()) {
            case GOTE -> gotePlayer.removeCapturedPiece(piece.getClass());
            case SENTE -> sentePlayer.removeCapturedPiece(piece.getClass());
        }
        changeTurn();
        moveCount++;
        history.addMove(new Move(null,pos,piece,null));
        boardChanged();
    }

    private void boardChanged() {
        boardChanged.set(!boardChanged.get());
    }

    public Side getTurn() {
        return turn;
    }

    public void promotePieceAt(Pos pos) {
        Piece piece = board.getPieceAt(pos);
        Side side = piece.getSide();
        if (piece instanceof Promotable && variant.inPromotionZone(pos, side.opposite())) {
            ((Promotable) piece).promote();
            // boardChanged();
        }
    }

}
