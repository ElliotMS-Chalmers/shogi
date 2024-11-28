package controller;

import javafx.scene.image.Image;
import model.Board;
import model.Game;
import util.Pos;
import util.Sfen;
import view.Piece;
import view.Theme;
import view.views.*;

public class GameController {
    private final Game game;
    private final GameView gameView;
    private SquareView lastSquareClicked;

    public GameController(Game game, GameView gameView) {
        this.game = game;
        this.gameView = gameView;
        gameView.setController(this);
        reDrawBoard();
    }

    private void movePiece(Pos from, Pos to) {
        Board board = game.getBoard();
        board.move(from, to);
        gameView.clearHighlightedSquares();
        lastSquareClicked = null;
        reDrawBoard();
    }

    private void reDrawBoard() {
        gameView.clearBoard();
        Sfen sfen = game.getSfen();
        drawBoard(sfen);
        gameView.drawHand(sfen);
    }

    private void drawBoard(Sfen sfen) {
        sfen.forEachPiece((abbr, pos) -> {
            // view.Piece piece =  Piece.fromSfenAbbreviation(abbr);
            // Image image = theme.getPieceSet().getImage(piece);
            gameView.drawPiece(Piece.fromSfenAbbreviation(abbr), pos);
        });
    }

    public void processBoardClick(BoardView.SquareView square) {
        Pos pos = square.getPos();
        if (lastSquareClicked instanceof PieceStandView.SquareView) {
            // play hand
            System.out.println("PieceStandView.SquareView");
        } else if (lastSquareClicked instanceof BoardView.SquareView) {
            Pos lastPos = ((BoardView.SquareView) lastSquareClicked).getPos();
            if (lastPos.equals(pos)) {
                gameView.clearHighlightedSquares();
                lastSquareClicked = null;
                return;
            }
            movePiece(lastPos, pos);
            System.out.println("BoardView.SquareView");
        } else { // == null
            boolean pieceClicked = game.getBoard().getPieceAt(pos) != null;
            if (pieceClicked) {
                lastSquareClicked = square;
                gameView.highlightSquare(pos);
            }
            System.out.println("null");
        }
    }

    public void processHandClick(PieceStandView.SquareView square) {
        if (square.equals(lastSquareClicked)) {
            lastSquareClicked = null;
            square.unHighlight();
            return;
        }
        lastSquareClicked = square;
        square.highlight();
    }
}
