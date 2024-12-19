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

/**
 * Represents a game of Shogi (Japanese chess), managing the state of the game, the players, 
 * the board, the rules, and the move history.
 */
public class Game {
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

    /**
     * Constructs a new game with the specified variant and time settings.
     * Initializes the board, players, clocks, and rule set.
     * 
     * @param variant The variant of the game (ruleset and board size).
     * @param time The time limit for the players, in seconds. Pass 0 for no time limit.
     */
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

    /**
     * Constructs a new game from a saved file.
     * Initializes the board, players, clocks, and rule set based on the saved game state.
     * 
     * @param saveFile The saved game file.
     */
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
        saveFile.getSfen().forEachCapturedPiece((abbr, amount) -> {
            Piece piece = PieceFactory.fromSfenAbbreviation(String.valueOf(abbr));
            switch (piece.getSide()) {
                case SENTE: sentePlayer.addCapturedPiece(piece.getClass());
                case GOTE: gotePlayer.addCapturedPiece(piece.getClass());
            }
        });

        this.moveCount = saveFile.getSfen().getMoveCount();
        this.turn = saveFile.getSfen().getTurn() == 'b' ? Side.SENTE : Side.GOTE;

        int senteTime = saveFile.getTime(Side.SENTE);
        int goteTime = saveFile.getTime(Side.GOTE);
        if (senteTime != 0 && goteTime != 0) {
            setClock(Side.SENTE, senteTime);
            setClock(Side.GOTE, goteTime);
            startClocks();
        }

        shutdownHook();
    }

    /**
     * Executes a move from one position to another, if the move is valid.
     * Updates the board, captured pieces, turn, move count, and history.
     * 
     * @param from The starting position of the move.
     * @param to The target position of the move.
     * @return The resulting move, or null if the move is invalid.
     */
    public Move move(Pos from, Pos to){
        if (board.getPieceAt(from) == null || !ruleSet.validMove(from, to, board.getPieceAt(from), board, turn, oppositeTurn)) { return null; }
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

    /**
     * Registers a shutdown hook to stop clocks and ensure clock threads finish before application shutdown.
     * This is useful for cleanly handling game shutdown scenarios.
     */
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

    /**
     * Returns the time left for the specified player as an {@link IntegerProperty}.
     * 
     * @param side The side (SENTE or GOTE) whose time is requested.
     * @return The time left for the specified player.
     */
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

    /**
     * Returns the time left for the specified player.
     * 
     * @param side The side (SENTE or GOTE) whose time is requested.
     * @return The time left for the specified player in seconds. Returns 0 if clocks are not initialized.
     */
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

    /**
     * Checks if both clocks for the players have been initialized.
     * 
     * @return True if both clocks are initialized, otherwise false.
     */
    public boolean isClocksInitialized() {
        return (senteClock != null && goteClock != null);
    }

    /**
     * Returns the current state of the game (whether it is running or not).
     * 
     * @return An AtomicBoolean indicating the running state of the game.
     */
    public AtomicBoolean getGameRunning() {
        return gameRunning;
    }

    /**
     * Returns the current game board.
     * 
     * @return The game board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the move history of the game.
     * 
     * @return The move history of the game.
     */
    public History getHistory() {return history;}

    /**
     * Returns the BooleanProperty that tracks if the board state has changed.
     * 
     * @return The BooleanProperty for the board change state.
     */
    public BooleanProperty boardChangedProperty() {
        return boardChanged;
    }

    /**
     * Returns the variant of the game, which includes the rule set and board configuration.
     * 
     * @return The variant of the game.
     */
    public Variant getVariant() {
        return variant;
    }

        /**
     * Returns the current SFEN (Shogi Forsyth-Edwards Notation) string representing the game's state.
     * 
     * @return The current SFEN string.
     */
    public Sfen getSfen() {
        return new Sfen(board.getBoardAsSfen(), turn == Side.SENTE ? 'b' : 'w', getCapturedPiecesAsSfen(), moveCount);
    }

    /**
     * Sets the clocks for both players to the specified time in seconds.
     * 
     * @param seconds The initial time in seconds for both players' clocks.
     */
    public void setClocks(int seconds) {
        if (seconds != 0) {
            this.senteClock = new Clock(seconds, Side.SENTE, gameRunning);
            this.goteClock = new Clock(seconds, Side.GOTE, gameRunning);
        }
    }

    /**
     * Sets the clock for the specified player to the given time in seconds.
     * 
     * @param side The side (SENTE or GOTE) for which to set the clock.
     * @param seconds The initial time in seconds for the specified player's clock.
     */
    public void setClock(Side side, int seconds) {
        if (seconds != 0) {
            switch (side) {
                case SENTE: this.senteClock = new Clock(seconds, Side.SENTE, gameRunning); break;
                case GOTE: this.goteClock = new Clock(seconds, Side.GOTE, gameRunning); break;
            }
        }
    }

    /**
     * Starts both players' clocks and sets the active clock based on the current turn.
     */
    public void startClocks() {
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

    /**
     * Stops both players' clocks and interrupts the clock threads.
     */
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

    /**
     * Undoes the last move, returning the game to its previous state.
     * This includes restoring the board, the captured pieces, and the move count.
     */
    public void undo() {
        Move lastMove = history.removeLast();
        changeTurn();
        moveCount--;
        if (lastMove.fromPlayerHand()) {
            (turn == Side.SENTE ? sentePlayer : gotePlayer).addCapturedPiece(lastMove.movedPiece().getClass());
        } else {
            board.move(lastMove.to(), lastMove.from()); // Plays the last move in reverse.
        }
        if (lastMove.capturedPiece() != null) {
            (turn == Side.SENTE ? sentePlayer : gotePlayer).removeCapturedPiece(lastMove.capturedPiece().getClass());
        }
        board.setAtPosition(lastMove.to(), lastMove.capturedPiece());
        boardChanged();
    }

    /**
     * Returns the captured pieces from both players as an SFEN string.
     * 
     * @return The captured pieces as an SFEN string.
     */
    public String getCapturedPiecesAsSfen() {
        String sfen = sentePlayer.getHandAsSfen() + gotePlayer.getHandAsSfen();
        return sfen.isEmpty() ? "-" : sfen;
    }

    /**
     * Sets the captured pieces from an SFEN string.
     * This updates the captured pieces for both players.
     * 
     * @param sfen The SFEN string representing captured pieces.
     */
    public void setCapturedPiecesFromSfen(String sfen) {
        int amount = 1;
        char c;
        for (int i = 0; i < sfen.length(); i++) {
            c = sfen.charAt(i);
            if (Character.isDigit(c)) {
                amount = (int) c;
                continue;
            }
            Class<? extends Piece> pieceClass = PieceFactory.fromSfenAbbreviation(String.valueOf(Character.toUpperCase(c))).getClass();
            if (Character.isUpperCase(c)) {
                sentePlayer.addCapturedPiece(pieceClass, amount);
            } else {
                gotePlayer.addCapturedPiece(pieceClass, amount);
            }
            amount = 1;
        }
    }

    /**
     * Switches the turn to the opposite player and updates the active clock accordingly.
     */
    private void changeTurn() {
        turn = switch (turn) {
            case SENTE -> Side.GOTE;
            case GOTE -> Side.SENTE;
        };
        oppositeTurn = switch (oppositeTurn) {
            case SENTE -> Side.GOTE;
            case GOTE -> Side.SENTE;
        };
        if (senteClock != null && goteClock != null)
            changeActiveClock();
    }

    /**
     * Switches the active clock based on the current turn.
     */
    private void changeActiveClock() {
        switch (turn) {
            case SENTE -> { senteClock.resume(); goteClock.pause(); }
            case GOTE -> { goteClock.resume(); senteClock.pause(); }
        }
    }

    /**
     * Places a piece from a player's hand onto the board at the specified position.
     * This also handles removing the piece from the player's hand and changing the turn.
     * 
     * @param pos The position on the board where the piece will be placed.
     * @param piece The piece to be played.
     */
    public void playHand(Pos pos, Piece piece) {
        board.setAtPosition(pos, piece);
        switch (piece.getSide()) {
            case GOTE -> gotePlayer.removeCapturedPiece(piece.getClass());
            case SENTE -> sentePlayer.removeCapturedPiece(piece.getClass());
        }
        changeTurn();
        moveCount++;
        history.addMove(new Move(null, pos, piece, null));
        boardChanged();
    }

    /**
     * Toggles the boardChanged property to indicate that the board has changed.
     */
    private void boardChanged() {
        boardChanged.set(!boardChanged.get());
    }

    /**
     * Returns the current side's turn.
     * 
     * @return The current side's turn (SENTE or GOTE).
     */
    public Side getTurn() {
        return turn;
    }

    /**
     * Promotes a piece at the specified position if it is eligible for promotion.
     * This checks if the piece is in the promotion zone and is of a type that can be promoted.
     * 
     * @param pos The position of the piece to promote.
     */
    public void promotePieceAt(Pos pos) {
        Piece piece = board.getPieceAt(pos);
        Side side = piece.getSide();
        if (piece instanceof Promotable && variant.inPromotionZone(pos, side.opposite())) {
            ((Promotable) piece).promote();
        }
    }

}
