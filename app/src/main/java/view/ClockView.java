package view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ClockView extends VBox {
    private Label timerLabel;

    public ClockView() {
        super();
        this.timerLabel = new Label("Time Left: 0");
        this.getChildren().add(timerLabel);
    }

    public void bindToTimer(IntegerProperty timerProperty) {
        // Bind the label to the timer property
        timerProperty.addListener((observable, oldValue, newValue) ->
                javafx.application.Platform.runLater(() ->
                        timerLabel.setText("Time Left: " + newValue)
                )
        );
    }



}
