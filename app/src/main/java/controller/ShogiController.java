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

import java.util.Arrays;
import java.util.List;

public class ShogiController {
    Settings settings;
    private final Game game;
    private final ShogiView shogiView;
    private final BoardView boardView;
    private final PieceStandView gotePieceStandView;
    private final PieceStandView sentePieceStandView;
    private SquareView lastSquareClicked;

    public ShogiController(Settings settings, Game game, ShogiView shogiView) {
        this.settings = settings;
        this.game = game;

        this.shogiView = shogiView;
        this.boardView = shogiView.getBoardView();
        this.gotePieceStandView = shogiView.getGotePieceStandView();
        this.sentePieceStandView = shogiView.getSentePieceStandView();

        boardView.setClickHandler(this::processBoardClick);
        gotePieceStandView.setClickHandler(this::processHandClick);
        sentePieceStandView.setClickHandler(this::processHandClick);

        setBackground();
        redraw();
    }

    private void setBackground() {
        boardView.setBackground(settings.getBoardTheme().getImage());
    }

    private void redraw() {
        boardView.clearPieces();
        Sfen sfen = game.getSfen();
        drawBoard(sfen);
        updateHands(sfen);
        drawHands(sfen);
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
            Image image = settings.getPieceSet().getImage(piece);
            boardView.drawImageAt(image, pos);
        });
    }

    private void drawHands(Sfen sfen) {
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
                int index = hand.indexOf(piece.getClass());
                sentePieceStandView.setCountAt(count, game.getVariant().getHand().size() - index - 1);
            } else {
                int index = hand.indexOf(piece.getClass());
                gotePieceStandView.setCountAt(count, index);
            }
        });
    }

    public void processBoardClick(BoardView.SquareView square) {
        Pos pos = square.getPos();
        List<Class<? extends Piece>> hand = game.getVariant().getHand();
        if (lastSquareClicked instanceof PieceStandView.SquareView) {
            Character[] gotePieceList = new Character[] {'r', 'b', 'g', 's', 'n', 'l', 'p'}; // TEMPORARY
            Character[] sentePieceList = new Character[] {'R', 'B', 'G', 'S', 'N', 'L', 'P'}; // TEMPORARY
            Side side = ((PieceStandView.SquareView) lastSquareClicked).getSide();
            int index = ((PieceStandView.SquareView) lastSquareClicked).getIndex();
            switch (side) {
                case GOTE -> game.playHand(pos, PieceFactory.fromClass(hand.get(index), side));
                case SENTE -> game.playHand(pos, PieceFactory.fromClass(hand.get(game.getVariant().getHand().size() - index - 1), side));
            }
            lastSquareClicked.unHighlight();
            lastSquareClicked = null;
            redraw();

        } else if (lastSquareClicked instanceof BoardView.SquareView) {
            Pos lastPos = ((BoardView.SquareView) lastSquareClicked).getPos();
            if (lastPos.equals(pos)) {
                boardView.clearHighlightedSquares();
                lastSquareClicked = null;
                return;
            }
            movePiece(lastPos, pos);
        } else { // == null
            boolean pieceClicked = game.getBoard().getPieceAt(pos) != null;
            if (pieceClicked) {
                lastSquareClicked = square;
                boardView.highlightSquare(pos);
            }
        }
    }

    public void processHandClick(PieceStandView.SquareView square) {
        if (square.equals(lastSquareClicked)) {
            lastSquareClicked = null;
            square.unHighlight();
            return;
        }
        if (lastSquareClicked != null) {
            lastSquareClicked.unHighlight();
        }
        lastSquareClicked = square;
        square.highlight();
    }
}
