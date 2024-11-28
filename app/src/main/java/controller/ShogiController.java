package controller;

import javafx.scene.image.Image;
import model.Game;
import model.Settings;
import util.Piece;
import util.Pos;
import util.Sfen;
import util.Side;
import view.*;

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
        render();
    }

    private void setBackground() {
        boardView.setBackground(settings.getBoardTheme().getImage());
    }

    private void movePiece(Pos from, Pos to) {
        game.getBoard().move(from, to);
        boardView.clearHighlightedSquares();
        lastSquareClicked = null;
        render();
    }

    private void render() {
        boardView.clearBoard();
        Sfen sfen = game.getSfen();
        drawBoard(sfen);
        drawHands(sfen);
        updateHands(sfen);
    }

    private void drawBoard(Sfen sfen) {
        sfen.forEachPiece((abbr, pos) -> {
            Piece piece =  Piece.fromSfenAbbreviation(abbr);
            Image image = settings.getPieceSet().getImage(piece);
            boardView.drawImageAt(image, pos);
        });
    }

    private void drawHands(Sfen sfen) {
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.GOTE_PAWN), 0);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.GOTE_LANCE), 1);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.GOTE_KNIGHT), 2);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.GOTE_SILVER_GENERAL), 3);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.GOTE_GOLD_GENERAL), 4);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.GOTE_BISHOP), 5);
        gotePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.GOTE_ROOK), 6);

        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.SENTE_ROOK), 0);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.SENTE_BISHOP), 1);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.SENTE_GOLD_GENERAL), 2);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.SENTE_SILVER_GENERAL), 3);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.SENTE_KNIGHT), 4);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.SENTE_LANCE), 5);
        sentePieceStandView.drawImageAt(settings.getPieceSet().getImage(Piece.SENTE_PAWN), 6);
    }

    private void updateHands(Sfen sfen) {
        Character[] gotePieceList = new Character[] {'p', 'l', 'n', 's', 'g', 'b', 'r'}; // TEMPORARY
        Character[] sentePieceList = new Character[] {'R', 'B', 'G', 'S', 'N', 'L', 'P'}; // TEMPORARY

        String capturedPieces = sfen.getCapturedPieces();
        for (int i = 0; i < capturedPieces.length()-1; i++) {
            char ch = capturedPieces.charAt(i);
            int count = 1;
            if (Character.isDigit(ch)) {
                count = Character.getNumericValue(ch);
                char nextCh = capturedPieces.charAt(++i);
                ch = nextCh;
            }
            if (Character.isUpperCase(ch)) {
                int index = Arrays.asList(sentePieceList).indexOf(ch);
                sentePieceStandView.setCountAt(count, index);
            } else {
                int index = Arrays.asList(gotePieceList).indexOf(ch);
                gotePieceStandView.setCountAt(count, index);
            }
        }
    }

    public void processBoardClick(BoardView.SquareView square) {
        Pos pos = square.getPos();
        if (lastSquareClicked instanceof PieceStandView.SquareView) {
            Character[] gotePieceList = new Character[] {'p', 'l', 'n', 's', 'g', 'b', 'r'}; // TEMPORARY
            Character[] sentePieceList = new Character[] {'R', 'B', 'G', 'S', 'N', 'L', 'P'}; // TEMPORARY
            Side side = ((PieceStandView.SquareView) lastSquareClicked).getSide();
            int index = ((PieceStandView.SquareView) lastSquareClicked).getIndex();
            switch (side) {
                case GOTE: /* play hand */ break;
                case SENTE: /* play hand */ break;
            }


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
