package controller;

import javafx.scene.image.Image;
import model.Game;
import model.PieceFactory;
import model.Settings;
// import util.Piece;
import util.Pos;
import model.Sfen;
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

    private final HistoryView historyView;

    private SquareView lastSquareClicked;

    public ShogiController(Settings settings, Game game, ShogiView shogiView) {
        this.settings = settings;
        this.game = game;

        this.shogiView = shogiView;
        this.boardView = shogiView.getBoardView();
        this.gotePieceStandView = shogiView.getGotePieceStandView();
        this.sentePieceStandView = shogiView.getSentePieceStandView();
        this.historyView = shogiView.getHistoryView();

        boardView.setClickHandler(this::processBoardClick);
        gotePieceStandView.setClickHandler(this::processHandClick);
        sentePieceStandView.setClickHandler(this::processHandClick);

        setBackground();
        drawHands();
        redraw();
    }

    private void setBackground() {
        boardView.setBackground(settings.getBoardTheme().getImage());
    }

    private void redraw() {
        boardView.clearPieces();
        boardView.clearMarkedSquares();
        Sfen sfen = game.getSfen();
        drawBoard(sfen);
        updateHands(sfen);
    }

    private void movePiece(Pos from, Pos to) {
        game.move(from, to);
        boardView.clearHighlightedSquares();
        lastSquareClicked = null;
        redraw();
    }


    private void drawBoard(Sfen sfen) {
        sfen.forEachPiece((abbr, pos) -> {
            Piece piece =  PieceFactory.fromSfenAbbreviation(abbr);
            // System.out.println(pos.row() + ", " + pos.col());
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
        for (int i = 0; i < 7; i++) {
            sentePieceStandView.setCountAt(0, i);
            gotePieceStandView.setCountAt(0, i);
        }

        List<Class<? extends Piece>> hand = game.getVariant().getHand();
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
        if (event.getButton() == MouseButton.SECONDARY) {
            Piece piece = game.getBoard().getPieceAt(pos);
            if (piece instanceof Promotable) {
                ((Promotable) piece).promote();
            }
            redraw();
            return;
        }

        List<Class<? extends Piece>> hand = game.getVariant().getHand();
        if (lastSquareClicked instanceof PieceStandView.SquareView) {
            Side side = ((PieceStandView.SquareView) lastSquareClicked).getSide();
            int index = ((PieceStandView.SquareView) lastSquareClicked).getIndex();
            switch (side) {
                case GOTE -> game.playHand(pos, PieceFactory.fromClass(hand.get(index), side));
                case SENTE -> game.playHand(pos, PieceFactory.fromClass(hand.get(game.getVariant().getHand().size() - index - 1), side));
            }
            lastSquareClicked.unHighlight();
            boardView.clearMarkedSquares();
            lastSquareClicked = null;
            redraw();

        } else if (lastSquareClicked instanceof BoardView.SquareView) {
            Pos lastPos = ((BoardView.SquareView) lastSquareClicked).getPos();
            if (lastPos.equals(pos)) {
                boardView.clearHighlightedSquares();
                boardView.clearMarkedSquares();
                lastSquareClicked = null;
                return;
            }
            movePiece(lastPos, pos);
        } else { // == null
            boolean pieceClicked = game.getBoard().getPieceAt(pos) != null;
            if (pieceClicked) {
                lastSquareClicked = square;
                boardView.highlightSquare(pos);
                Piece piece = game.getBoard().getPieceAt(pos);
                piece.getAvailableMoves(pos).forEach(boardView::markSquare);
            }
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
}
