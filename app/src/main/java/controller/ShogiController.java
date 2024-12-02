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
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(new Pawn(Side.GOTE)), 0);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(new Lance(Side.GOTE)), 1);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(new Knight(Side.GOTE)), 2);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(new SilverGeneral(Side.GOTE)), 3);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(new GoldGeneral(Side.GOTE)), 4);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(new Bishop(Side.GOTE)), 5);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(new Rook(Side.GOTE)), 6);

        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(new Rook(Side.SENTE)), 0);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(new Bishop(Side.SENTE)), 1);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(new GoldGeneral(Side.SENTE)), 2);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(new SilverGeneral(Side.SENTE)), 3);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(new Knight(Side.SENTE)), 4);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(new Lance(Side.SENTE)), 5);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(new Pawn(Side.SENTE)), 6);
    }

    private void updateHands(Sfen sfen) {
        Character[] gotePieceList = new Character[] {'p', 'l', 'n', 's', 'g', 'b', 'r'}; // TEMPORARY
        Character[] sentePieceList = new Character[] {'R', 'B', 'G', 'S', 'N', 'L', 'P'}; // TEMPORARY

        for (int i = 0; i < 7; i++) {
            sentePieceStandView.setCountAt(0, i);
            gotePieceStandView.setCountAt(0, i);
        }

        sfen.forEachCapturedPiece((abbr, count) -> {
            if (Character.isUpperCase(abbr)) {
                int index = Arrays.asList(sentePieceList).indexOf(abbr);
                sentePieceStandView.setCountAt(count, index);
                System.out.println(count + ", " + index);
            } else {
                int index = Arrays.asList(gotePieceList).indexOf(abbr);
                gotePieceStandView.setCountAt(count, index);
                System.out.println(count + ", " + index);
            }
        });
    }

    public void processBoardClick(BoardView.SquareView square) {
        Pos pos = square.getPos();
        if (lastSquareClicked instanceof PieceStandView.SquareView) {
            Character[] gotePieceList = new Character[] {'p', 'l', 'n', 's', 'g', 'b', 'r'}; // TEMPORARY
            Character[] sentePieceList = new Character[] {'R', 'B', 'G', 'S', 'N', 'L', 'P'}; // TEMPORARY
            Side side = ((PieceStandView.SquareView) lastSquareClicked).getSide();
            int index = ((PieceStandView.SquareView) lastSquareClicked).getIndex();
            switch (side) {
                case GOTE -> game.playHand(pos, PieceFactory.fromSfenAbbreviation(String.valueOf(gotePieceList[index])));
                case SENTE -> game.playHand(pos, PieceFactory.fromSfenAbbreviation(String.valueOf(sentePieceList[index])));
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
