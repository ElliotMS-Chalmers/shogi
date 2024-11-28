package controller;

import model.Board;
import model.Game;
import util.Pos;
import util.Sfen;
import view.Piece;
import view.views.*;

public class GameController {
    private final Game game;
    private final GameView gameView;
    private Pos lastBoardPosClicked;
    private Piece lastHandPieceClicked;

    public GameController(Game game, GameView gameView) {
        this.game = game;
        this.gameView = gameView;
        gameView.setController(this);
        reDrawBoard();
    }

    public void processBoardClick(Pos pos) {
        if (lastBoardPosClicked == null) {
            boolean pieceClicked = game.getBoard().getPieceAt(pos) != null;
            if (pieceClicked) {
                lastBoardPosClicked = pos;
                gameView.highlightSquare(pos);
            }
            return;
        }
        if (lastBoardPosClicked.equals(pos)) {
            lastBoardPosClicked = null;
            gameView.clearHighlightedSquares();
            return;
        }

        movePiece(lastBoardPosClicked, pos);
    }

    private void movePiece(Pos from, Pos to) {
        game.move(from, to);
        gameView.clearHighlightedSquares();
        lastBoardPosClicked = null;
        reDrawBoard();
    }

    private void reDrawBoard() {
        gameView.clearBoard();
        Sfen sfen = game.getSfen();
        gameView.drawBoard(sfen);
        gameView.drawHand(sfen);
    }

    public void processHandClick(view.Piece piece) {
        System.out.println("Hand clicked: " + piece.toString());
    }
}
