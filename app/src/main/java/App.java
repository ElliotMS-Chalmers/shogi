import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.Game;
import model.variants.Standard;
import variants.Variant;
import util.*;
import view.*;

public class App extends Application {
	public void start(Stage stage) {
        Settings settings = new Settings("/settings.properties");
        ThemeManager themeManager = new ThemeManager();
        Theme theme = new Theme(
                themeManager.getPieceSet(settings.getPieceSet()),
                themeManager.getBoardTheme(settings.getBoardTheme())
        );

		Variant gameMode = new Standard();
		Game game = new Game(gameMode);

        BoardView board = new BoardView(9, theme);
        PieceStandView gotePieceStand = new PieceStandView(Side.GOTE, theme);
        PieceStandView sentePieceStand = new PieceStandView(Side.SENTE, theme);
		GameView gameView = new GameView(board, gotePieceStand, sentePieceStand);
		Scene scene = new Scene(gameView.getView(), 1280, 1000);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());


		stage.setTitle("Shogi v1.0");
		stage.setScene(scene);
        stage.setMinHeight(480);
        stage.setMinWidth(720);
		stage.show();

		// TESTING DRAW BOARD, something like this should be in controller
        board.drawPiece(Piece.GOTE_PROMOTED_SILVER, 4, 4);
		Sfen sfen = new Sfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b RG9S2NL5P2gs2n 1");
        board.drawBoard(sfen);
        gotePieceStand.drawHand(sfen);
        sentePieceStand.drawHand(sfen);
	}

    public static void main(String[] args) {
		launch(args);
    }
}
