package controller;

import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import model.*;
// import util.Piece;
import util.Pos;
import util.Side;
import view.*;
import model.pieces.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import java.util.List;

public class ShogiController {
    private final Settings settings;
    private final Game game;
    private final ShogiView shogiView;
    private final BoardView boardView;
    private final PieceStandView gotePieceStandView;
    private final PieceStandView sentePieceStandView;

    private Clock clock;
    private String selected = "1 min"; //test

    private SquareView lastSquareClicked;

    public ShogiController(Settings settings, Game game, ShogiView shogiView) {
        this.settings = settings;
        this.game = game;
        this.shogiView = shogiView;
        this.boardView = shogiView.getBoardView();
        this.gotePieceStandView = shogiView.getGotePieceStandView();
        this.sentePieceStandView = shogiView.getSentePieceStandView();

        // Handle user interaction
        boardView.setClickHandler(this::processBoardClick);
        gotePieceStandView.setClickHandler(this::processHandClick);
        sentePieceStandView.setClickHandler(this::processHandClick);

        // Handle change to board
        game.boardChangedProperty().addListener(this::onBoardChanged);

        // Handle change in settings
        settings.boardThemeProperty().addListener(this::onBoardThemeChanged);
        settings.pieceSetProperty().addListener(this::onPieceSetChanged);

        // Setup
        setClock();
        setBackground();
        drawHands();
        Sfen sfen = game.getSfen();
        drawBoard(sfen);
        updateHands(sfen);
        startClock();
    }

    private void setBackground() {
        boardView.setBackground(settings.getBoardTheme().getImage());
    }

    private void movePiece(Pos from, Pos to) {
        game.move(from, to);
        lastSquareClicked = null;
    }

    private void drawBoard(Sfen sfen) {
        sfen.forEachPiece((abbr, pos) -> {
            Piece piece =  PieceFactory.fromSfenAbbreviation(abbr);
            Image image = settings.getPieceSet().getImage(piece);
            boardView.drawImageAt(image, pos);
        });
    }


    private void drawHands() {
        List<Class<? extends Piece>> hand = game.getVariant().getHand();
        int i = 0;
        int j = hand.size()-1;
        for (Class<? extends Piece> pieceClass : hand) {
            Piece gotePiece = PieceFactory.fromClass(pieceClass, Side.GOTE);
            Piece sentePiece = PieceFactory.fromClass(pieceClass, Side.SENTE);
            gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(gotePiece), i);
            sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(sentePiece), j);
            i++; j--;
        }
    }

    private void updateHands(Sfen sfen) {
        // Clear piece counts
        for (int i = 0; i < 7; i++) {
            sentePieceStandView.setCountAt(0, i);
            gotePieceStandView.setCountAt(0, i);
        }

        List<Class<? extends Piece>> hand = game.getVariant().getHand();
        // Set new piece counts
        sfen.forEachCapturedPiece((abbr, count) -> {
            Piece piece = PieceFactory.fromSfenAbbreviation(String.valueOf(abbr));
            if (Character.isUpperCase(abbr)) {
                int index = game.getVariant().getHand().size() - hand.indexOf(piece.getClass()) - 1;
                sentePieceStandView.setCountAt(count, index);
            } else {
                int index = hand.indexOf(piece.getClass());
                gotePieceStandView.setCountAt(count, index);
            }
        });
    }

    public void processBoardClick(BoardView.SquareView square, MouseEvent event) {
        Pos pos = square.getPos();

        boolean isRightClick = event.getButton() == MouseButton.SECONDARY;
        if (isRightClick) {
            handleRightClick(pos);
            return;
        }

        if (lastSquareClicked instanceof PieceStandView.SquareView) {
            handlePieceStandClick(pos);
        } else if (lastSquareClicked instanceof BoardView.SquareView) {
            handleBoardSquareClick(pos);
        } else {
            handleFirstClick(square, pos);
        }
    }

    private void handleRightClick(Pos pos) {
        Piece piece = game.getBoard().getPieceAt(pos);
        if (piece instanceof Promotable) {
            ((Promotable) piece).promote();
        }
    }

    private void handlePieceStandClick(Pos pos) {
        List<Class<? extends Piece>> hand = game.getVariant().getHand();
        Side side = ((PieceStandView.SquareView) lastSquareClicked).getSide();
        int index = ((PieceStandView.SquareView) lastSquareClicked).getIndex();
        switch (side) {
            case GOTE -> game.playHand(pos, PieceFactory.fromClass(hand.get(index), side));
            case SENTE -> game.playHand(pos, PieceFactory.fromClass(hand.get(hand.size() - index - 1), side));
        }
        lastSquareClicked.unHighlight();
        boardView.clearMarkedSquares();
        lastSquareClicked = null;
    }

    private void handleBoardSquareClick(Pos pos) {
        Pos lastPos = ((BoardView.SquareView) lastSquareClicked).getPos();
        if (lastPos.equals(pos)) {
            boardView.clearHighlightedSquares();
            boardView.clearMarkedSquares();
            lastSquareClicked = null;
            return;
        }
        movePiece(lastPos, pos);
    }

    private void handleFirstClick(BoardView.SquareView square, Pos pos) {
        boolean pieceClicked = game.getBoard().getPieceAt(pos) != null;
        if (pieceClicked) {
            lastSquareClicked = square;
            boardView.highlightSquare(pos);
            Piece piece = game.getBoard().getPieceAt(pos);
            piece.getAvailableMoves(pos).forEach(boardView::markSquare);
        }
    }

    public void processHandClick(PieceStandView.SquareView square) {
        if (square.equals(lastSquareClicked)) {
            lastSquareClicked = null;
            square.unHighlight();
        } else if (lastSquareClicked != null) {
            lastSquareClicked.unHighlight();
        } else if (square.getCount() > 0) {
            lastSquareClicked = square;
            square.highlight();
        }
    }

    private void onBoardChanged(Observable observable) {
        boardView.clearPieces();
        boardView.clearHighlightedSquares();
        boardView.clearMarkedSquares();
        Sfen sfen = game.getSfen();
        drawBoard(sfen);
        updateHands(sfen); // the only time we change piece counts in hand is when capture pieces (ie board changed) so this is fine for now but not ideal
    }

    private void onBoardThemeChanged(Observable observable) {
        setBackground();
    }

    private void onPieceSetChanged(Observable observable) {
        drawHands();
        boardView.clearPieces();
        drawBoard(game.getSfen());
    }

    private void setClock() {
        int timeChosen = Integer.parseInt(selected.split(" ")[0]) * 60;
        this.clock = new Clock(timeChosen);
    }

    private void startClock(){
        Thread th = new Thread(this.clock);
        th.start();
    }
}
