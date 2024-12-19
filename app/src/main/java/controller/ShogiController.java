package controller;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import model.*;
// import util.Piece;
import model.settings.PieceSet;
import model.settings.PieceSetType;
import model.settings.Settings;
import util.Pos;
import util.Side;
import view.*;
import model.pieces.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import java.util.List;


public class ShogiController {
    private Settings settings;
    private Game game;
    private ShogiView shogiView;
    private BoardView boardView;
    private PieceStandView gotePieceStandView;
    private PieceStandView sentePieceStandView;
    private ClockView senteClockView, goteClockView;
    private HistoryController historyController;
    private SquareView lastSquareClicked;

    public ShogiController(Settings settings, Game game) {
        this.settings = settings;
        this.game = game;
        this.shogiView = new ShogiView(game.getVariant().getWidth(), game.getVariant().getHand().size());
        this.boardView = shogiView.getBoardView();
        this.gotePieceStandView = shogiView.getGotePieceStandView();
        this.sentePieceStandView = shogiView.getSentePieceStandView();
        this.senteClockView = shogiView.getSenteClockView();
        this.goteClockView = shogiView.getGoteClockView();

        // Handle user input
        boardView.setClickHandler(this::processBoardClick);
        gotePieceStandView.setClickHandler(this::processHandClick);
        sentePieceStandView.setClickHandler(this::processHandClick);

        // Handle change to board and game
        game.boardChangedProperty().addListener(this::onBoardChanged);

        // Handle change in settings
        settings.boardThemeProperty().addListener(this::onBoardThemeChanged);
        getPieceSetProperty().addListener(this::onPieceSetChanged);

        // Handle change in clocks
        if (game.isClocksInitialized()) {
            senteClockView.bindToTimer(game.timeProperty(Side.SENTE));
            goteClockView.bindToTimer(game.timeProperty(Side.GOTE));
            //game.getSenteTime().addListener(this::onSenteClockTimeChanged);
            //game.getGoteTime().addListener(this::onGoteClockTimeChanged);
        }

        historyController = new HistoryController(this, game, shogiView.getHistoryView());

        // Setup
        setBackground();
        drawHands();
        Sfen sfen = game.getSfen();
        drawBoard(sfen);
        updateHands(sfen);
    }

    private void setBackground() {
        boardView.setBackground(new Image(settings.getBoardTheme().getImage()));
    }

    private void movePiece(Pos from, Pos to) {
        Move move = game.move(from, to);
        lastSquareClicked.unHighlight();
        lastSquareClicked = null;
        boardView.clearMarkedSquares();
        if (move != null) {
            if (move.capturedPiece() != null) {
                SoundPlayer.playCaptureSound(settings.getSoundSet());
            } else {
                SoundPlayer.playMoveSound(settings.getSoundSet());
            }
        }
    }

    private void drawBoard(Sfen sfen) {
        sfen.forEachPiece((abbr, pos) -> {
            Piece piece =  PieceFactory.fromSfenAbbreviation(abbr);
            Image image = new Image(getPieceSet().getImage(piece));
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
            gotePieceStandView.drawImageAt(new Image(getPieceSet().getImage(gotePiece)), i);
            sentePieceStandView.drawImageAt(new Image(getPieceSet().getImage(sentePiece)), j);
            i++; j--;
        }
    }

    private void updateHands(Sfen sfen) {
        // Clear piece counts
        for (int i = 0; i < sentePieceStandView.getSize(); i++) {
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
            handleBoardRightClick(pos);
            return;
        }

        if (lastSquareClicked instanceof PieceStandView.SquareView) {
            handleHandToBoardClick(pos);
        } else if (lastSquareClicked instanceof BoardView.SquareView) {
            handleBoardToBoardClick(pos);
        } else {
            handleFirstClick(square, pos);
        }
    }

    private void handleBoardRightClick(Pos pos) {
        game.promotePieceAt(pos);
        onBoardChanged(null); // temp fix to redraw board on piece promotion
    }

    private void handleHandToBoardClick(Pos pos) {
        List<Class<? extends Piece>> hand = game.getVariant().getHand();
        Side side = ((PieceStandView.SquareView) lastSquareClicked).getSide();
        int index = ((PieceStandView.SquareView) lastSquareClicked).getIndex();
        switch (side) {
            case GOTE -> game.playHand(pos, PieceFactory.fromClass(hand.get(index), side));
            case SENTE -> game.playHand(pos, PieceFactory.fromClass(hand.get(hand.size() - index - 1), side));
        }
        lastSquareClicked.unHighlight();
        lastSquareClicked = null;
        boardView.clearMarkedSquares();
        SoundPlayer.playMoveSound(settings.getSoundSet());
    }

    private void handleBoardToBoardClick(Pos pos) {
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
        Piece pieceClicked = game.getBoard().getPieceAt(pos);
        if (pieceClicked != null) {
            if (pieceClicked.getSide() != game.getTurn()) return;
            lastSquareClicked = square;
            boardView.highlightSquare(pos);
            Piece piece = game.getBoard().getPieceAt(pos);
            piece.getAvailableMoves(pos, game.getBoard()).forEach(boardView::markSquare);
        }
        historyController.highlightLastMove();
    }

    public void processHandClick(PieceStandView.SquareView square) {
        if (square.getSide() != game.getTurn()) return;
        if (square.equals(lastSquareClicked)) {
            lastSquareClicked = null;
            square.unHighlight();
        } else if (lastSquareClicked != null) {
            lastSquareClicked.unHighlight();
        } else if (square.getCount() > 0) {
            lastSquareClicked = square;
            square.highlight();
            // todo: highlight all available moves from hand
        }
        historyController.highlightLastMove();
    }

    //private void onSenteClockTimeChanged(Observable observable) { // Change so it can handle both SENTE and GOTE
      //  senteClockView.update();
   // }

    //private void onGoteClockTimeChanged(Observable observable) {
      //  goteClockView.update();
    //}

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

    public ShogiView getView() {
        return shogiView;
    }

    public void setBoardViewSquare(Piece piece,Pos pos){
        Image image;
        if(piece == null){image = null;}
        else{image = new Image(getPieceSet().getImage(piece));}
        boardView.drawImageAt(image,pos);
    }

    public void clearHighlightedSquares(){boardView.clearHighlightedSquares();}

    public void highlightSquare(Pos pos){boardView.highlightSquare(pos);}

    public void changeCountAtPieceStandView(Class<?extends Piece> pieceClass,Side side,int change){
        List<Class<? extends Piece>> hand = game.getVariant().getHand();
        int index;
        //Pjäserna i PieceStandView är i omvänd ordning för sente
        if(side == Side.GOTE){index = hand.indexOf(pieceClass);}
        else{index = hand.size() - hand.indexOf(pieceClass) - 1;}
        PieceStandView pieceStandView = (side == Side.SENTE) ? sentePieceStandView : gotePieceStandView;
        pieceStandView.changeCountAt(index,change);
    }

    public void unselectsSquare(){
        //This is used by HistoryController to prevent moving while viewing a past state
        lastSquareClicked = null;
        boardView.clearMarkedSquares();
        sentePieceStandView.unHighlightSquares();
        gotePieceStandView.unHighlightSquares();
    }

    private PieceSet getPieceSet() {
        return switch (game.getVariant().getPieceSetType()) {
            case PieceSetType.STANDARD -> settings.getStandardPieceSet();
            case PieceSetType.CHU -> settings.getChuPieceSet();
            case PieceSetType.KYO -> settings.getKyoPieceSet();
        };
    }

    private ObjectProperty<PieceSet> getPieceSetProperty() {
        return switch (game.getVariant().getPieceSetType()) {
            case PieceSetType.STANDARD -> settings.standardPieceSetProperty();
            case PieceSetType.CHU -> settings.chuPieceSetProperty();
            case PieceSetType.KYO -> settings.kyoPieceSetProperty();
        };
    }
}
