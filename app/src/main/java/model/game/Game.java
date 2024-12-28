package model.game;

import model.*;
import model.pieces.*;
import model.variants.RuleSet;
import model.variants.Variant;
import util.Pos;
import util.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Represents a game of Shogi (Japanese chess), managing the state of the game, the players, 
 * the board, the rules, and the move history.
 */
public class Game {
    /**
     * The current player's turn (SENTE or GOTE).
     */
    private Side turn = Side.SENTE;

    /**
     * The variant of the game, including rules and board configuration.
     */
    private Variant variant;

    /**
     * The game board representing the state of the pieces.
     */
    private Board board;

    /**
     * A property indicating whether the board state has changed.
     * Used for notifying listeners of updates to the board.
     */
    private BooleanProperty boardChanged = new SimpleBooleanProperty(false);

    /**
     * The player controlling the SENTE side.
     */
    private Player sentePlayer;

    /**
     * The player controlling the GOTE side.
     */
    private Player gotePlayer;

    /**
     * The clock for the SENTE player, tracking their remaining time.
     */
    private Clock senteClock;

    /**
     * The clock for the GOTE player, tracking their remaining time.
     */
    private Clock goteClock;

    /**
     * The thread managing the SENTE player's clock.
     */
    private Thread senteth;

    /**
     * The thread managing the GOTE player's clock.
     */
    private Thread goteth;

    /**
     * A flag indicating whether the game is running.
     * True if the game is active, false if it is paused or ended.
     */
    private AtomicBoolean gameRunning = new AtomicBoolean(true);

    /**
     * The number of moves that have been made in the game.
     */
    private int moveCount = 1;

    /**
     * The history of moves made in the game, stored for undo or analysis purposes.
     */
    private History history;

    /**
     * The rule set governing the game's mechanics and move validation.
     */
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
                case SENTE: sentePlayer.addCapturedPiece(piece.getClass(), amount); break;
                case GOTE: gotePlayer.addCapturedPiece(piece.getClass(), amount); break;
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
    public Move move(Pos from, Pos to) {
        if (board.getPieceAt(from) == null || !isValidMove(from, to)) { return null; }
        // boolean isPromotableMove = isPromotableMove(from, to);
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
     * Checks if a move is valid according to the current rule set.
     *
     * @param from The position on the board where piece is.
     * @param to   THe position on the board where piece wants to go.
     * @return True if the move is valid, otherwise false.
     */
    public boolean isValidMove(Pos from, Pos to) {
        return ruleSet.validMove(from, to, board.getPieceAt(from), board, turn, turn.opposite());
    }

    /**
     * Checks if a hand move (placing a piece from a player's captured pieces onto the board)
     * is valid according to the current rule set.
     *
     * @param pos   The position on the board where the piece is to be placed.
     * @param piece The piece to be placed.
     * @return True if the move is valid, otherwise false.
     */
    public boolean isValidHandMove(Pos pos, Piece piece) {
        return ruleSet.validHandMove(pos, piece.getClass(), board, turn);
    }

    /**
     * Returns all valid positions for placing a piece from a player's captured pieces onto the board.
     *
     * @param piece The piece to be placed.
     * @return A list of valid positions for the specified piece.
     */
    public List<Pos> getValidHandMovePositions(Piece piece) {
        List<Pos> positions = new ArrayList<>();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Pos pos = new Pos(i, j);
                if (isValidHandMove(pos, piece))
                    positions.add(pos);
            }
        }
        return positions;
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
        IntegerProperty time = timeProperty(side);
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
     * Switches the turn to the opposite player and updates the active clock accordingly.
     */
    private void changeTurn() {
        if(ruleSet.isCurrentlyInCheckMate(board, board.getPiecePos(turn.opposite(), King.class), turn.opposite(), turn, getOppositePlayer())){
            System.out.println("Checkmate!");
        }
        turn = switch (turn) {
            case SENTE -> Side.GOTE;
            case GOTE -> Side.SENTE;
        };
        if (isClocksInitialized())
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
     * Gets the clock for a specified player.
     *
     * @param side The side (SENTE or GOTE) whose clock is requested.
     * @return The {@link Clock} object for the specified side, or null if not initialized.
     */
    public Clock getClock(Side side) {
        switch (side) {
            case SENTE -> { return senteClock; }
            case GOTE -> { return goteClock; }
        }
        return null;
    }

    /**
     * Places a piece from a player's hand onto the board at the specified position.
     * This also handles removing the piece from the player's hand and changing the turn.
     * 
     * @param pos The position on the board where the piece will be placed.
     * @param piece The piece to be played.
     */
    public void playHand(Pos pos, Piece piece) {
        if (!isValidHandMove(pos, piece)){ return; }
        board.setAtPosition(pos, piece);
        switch (piece.getSide()) {
            case GOTE -> gotePlayer.removeCapturedPiece(piece.getClass());
            case SENTE -> sentePlayer.removeCapturedPiece(piece.getClass());
        }
        changeTurn();
        moveCount++;
        history.addMove(new Move(null, pos, piece, null, false));
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
     * Returns the number of moves occured
     * 
     * @return The number of moves done (returns the value of moveCount)
    */
    public int getMoveCount() {
        return moveCount;
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
        if (piece instanceof Promotable && variant.isInPromotionZone(pos, side.opposite())) {
            ((Promotable) piece).promote();
        }
    }

    public boolean isPromotableMove(Pos from, Pos to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null) return false;
        Side side = piece.getSide();
        if (piece instanceof Promotable && !((Promotable) piece).getIsPromoted()) {
            return variant.isInPromotionZone(from, side.opposite()) || variant.isInPromotionZone(to, side.opposite());
        }
        return false;
    }

    /**
     * Gets the player corresponding to a specific side.
     *
     * @param side The side (SENTE or GOTE) for which the player is requested.
     * @return The {@link Player} for the specified side.
     */
    public Player getPlayer(Side side) {
        switch (side) {
            case SENTE -> { return sentePlayer; }
            case GOTE -> { return gotePlayer; }
        }
        return null;
    }

    /**
     * Gets the player whose turn it is not (the opposite player).
     *
     * @return The {@link Player} whose turn it is not.
     */
    public Player getOppositePlayer() {
        switch (turn) {
            case SENTE -> { return gotePlayer; }
            case GOTE -> { return sentePlayer; }
        }
        return null;
    }
}
