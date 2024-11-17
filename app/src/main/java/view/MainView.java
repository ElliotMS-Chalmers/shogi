package view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

public class MainView {
	private final BorderPane root;

	public MainView() {
		root = new BorderPane();
		
		GridPane gridPane = new GridPane();
		root.setCenter(gridPane);
		
		gridPane.setStyle(
			"-fx-background-image: url('" + getClass().getResource("/wood1.jpg") + "'); " +
			"-fx-background-size: cover; " +
			"-fx-background-position: center; " +
			"-fx-border-width: 0.5; -fx-border-color: black"
		);
		
		gridPane.setMaxSize(750, 750);
		
		for (int i = 0; i < 9; i++) {
            ColumnConstraints colConstraint = new ColumnConstraints();
            colConstraint.setPercentWidth(100.0 / 9); // Each column takes up 1/9 of the width
            gridPane.getColumnConstraints().add(colConstraint);
        }

        // Define 9 row constraints, each taking 1/9 of the available height
        for (int i = 0; i < 9; i++) {
            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setPercentHeight(100.0 / 9); // Each row takes up 1/9 of the height
            gridPane.getRowConstraints().add(rowConstraint);
        }

		for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
				Pane cell = new Pane();
                cell.setStyle("-fx-border-color: black; -fx-border-width: 0.5;"); // Thin black border around each cell
                gridPane.add(cell, col, row);
            }
        }
	}

	public BorderPane getView() {
		return root;
	}
}
